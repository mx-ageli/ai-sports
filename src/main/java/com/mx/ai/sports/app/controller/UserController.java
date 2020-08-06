package com.mx.ai.sports.app.controller;

import com.aliyuncs.exceptions.ClientException;
import com.mx.ai.sports.app.api.UserApi;
import com.mx.ai.sports.common.annotation.Limit;
import com.mx.ai.sports.common.annotation.Log;
import com.mx.ai.sports.common.controller.BaseRestController;
import com.mx.ai.sports.common.entity.AiSportsConstant;
import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.common.entity.LimitType;
import com.mx.ai.sports.common.entity.RoleEnum;
import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.common.oss.OssUploadUtil;
import com.mx.ai.sports.common.utils.*;
import com.mx.ai.sports.monitor.entity.LoginLog;
import com.mx.ai.sports.monitor.service.ILoginLogService;
import com.mx.ai.sports.system.converter.UserConverter;
import com.mx.ai.sports.system.vo.UserSimple;
import com.mx.ai.sports.system.vo.UserVo;
import com.mx.ai.sports.system.entity.TeacherRegister;
import com.mx.ai.sports.system.entity.User;
import com.mx.ai.sports.system.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.File;
import java.util.Arrays;
import java.util.Date;

import static com.mx.ai.sports.common.entity.AiSportsConstant.*;


/**
 * 用户相关接口
 *
 * @author Mengjiaxin
 * @date 2019-08-28 16:04
 */
@Slf4j
@RestController("UserApi")
public class UserController extends BaseRestController implements UserApi {

    @Autowired
    private JedisPoolUtil jedisPoolUtil;

    @Autowired
    private ILoginLogService loginLogService;

    @Autowired
    private IUserService userService;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private SmsUtil smsUtil;

    @Autowired
    private OssUploadUtil ossUploadUtil;


    @Override
    @Log("手机号和短信验证码登录获得token")
    @Limit(key = "mobileLogin", period = 60, count = 3, name = "登录", prefix = "limit")
    public AiSportsResponse<String> login(@NotBlank @Pattern(regexp = AccountValidatorUtil.REGEX_MOBILE, message = "格式不正确") @RequestParam("mobile") String mobile,
                                          @NotBlank @Length(min = 6, max = 6, message = "长度必须等于6位") @RequestParam("code") String code,
                                          @NotBlank @RequestParam("deviceId") String deviceId) throws AiSportsException {
        boolean isMobile = AccountValidatorUtil.isMobile(mobile);
        if (!isMobile) {
            return new AiSportsResponse<String>().fail().message("手机号格式不正确!");
        }

        // 根据phone从redis中取出发送的短信验证码，并与用户输入的验证码比较
        String messageCode = jedisPoolUtil.get(mobile);

        if (StringUtils.isEmpty(messageCode)) {
            return new AiSportsResponse<String>().fail().message("验证码过期,请重新获取!");
        } else if (!messageCode.equals(code)) {
            return new AiSportsResponse<String>().fail().message("验证码错误!");
        } else {
            // 检查用户是否已经注册，如果没有注册默认系统注册
            User user = checkUserAndRegister(mobile, deviceId);
            // 保存登录日志
            mobileLoginLog(mobile);
            // 生成token
            String token = JwtTokenUtil.generateToken(userConverter.domain2Simple(user));
            // 如果传入有设备Id，将设备Id更新保存
            if (!StringUtils.isEmpty(deviceId)) {
                User updateUser = userService.getById(user.getUserId());
                updateUser.setDeviceId(deviceId);
                userService.saveOrUpdate(updateUser);
            }

            // 登录成功后,删除验证码
            jedisPoolUtil.del(mobile);

            return new AiSportsResponse<String>().success().data(token);
        }
    }

    /**
     * 检查用户是否已经注册，如果没有注册默认系统注册
     *
     * @param mobile
     * @param deviceId
     * @return
     */
    private User checkUserAndRegister(String mobile, String deviceId) {
        // 先通过手机号查询用户信息
        User user = userService.findByUsername(mobile);
        // 用户还没有注册，系统给默认注册
        if (user == null) {
            user = new User();
            user.setUsername(mobile);
            user.setCreateTime(new Date());
            user.setModifyTime(new Date());
            user.setLastLoginTime(new Date());
            user.setDeviceId(deviceId);
            user.setSchoolId(DEFAULT_SCHOOL_ID);
            user.setAvatar(DEFAULT_AVATAR);

            // 检查这个用户是不是老师
            TeacherRegister teacherRegister = userService.findTeacherRegisterByUsername(mobile);
            // 存在说明为老师
            if (teacherRegister != null) {
                // 使用老师登记的姓名
                user.setFullName(teacherRegister.getFullName());
                user.setRoleId(RoleEnum.TEACHER.value());

                teacherRegister.setIsRegister(Boolean.TRUE);
                userService.updateRegisterTeacher(teacherRegister);
            } else {
                user.setRoleId(RoleEnum.STUDENT.value());
            }

            userService.save(user);
        }
        return user;
    }

    /**
     * 保存登录日志
     *
     * @param mobile 手机号
     */
    private void mobileLoginLog(String mobile) {
        // 保存登录日志
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(mobile);
        loginLog.setSystemBrowserInfo();
        this.loginLogService.saveLoginLog(loginLog);
    }

    @Override
    @Log("获取手机验证码")
    @Limit(key = "getCode", period = 60, count = 3, name = "获取手机验证码", prefix = "limit", limitType = LimitType.IP)
    public AiSportsResponse<Boolean> getCode(@NotBlank @Pattern(regexp = AccountValidatorUtil.REGEX_MOBILE, message = "格式不正确") @RequestParam("mobile") String mobile) throws AiSportsException {
        boolean isMobile = AccountValidatorUtil.isMobile(mobile);
        if (!isMobile) {
            return new AiSportsResponse<Boolean>().fail().message("手机号格式不正确!");
        }
        String keyMobile = CODE_DATE_OUT + mobile;
        // 先判断key是否存在，不存在则直接发送短信
        Boolean isExists = jedisPoolUtil.exists(keyMobile);
        if (isExists) {
            // key存在，获取过期时间
            Long ttlMobile = jedisPoolUtil.ttl(keyMobile);
            log.info("手机号:{}, 正在重复获取验证码, 间隔时间: {} 秒, 请求默认拒绝!", mobile, ttlMobile);
            String msg = "获取验证码太过频繁, 请等待" + ttlMobile + "秒后再次获取！";
            return new AiSportsResponse<Boolean>().message(msg).fail().data(Boolean.FALSE);
        }

        String code = JwtTokenUtil.getRandomCode();
        code = "666666";
        log.info("手机号:{}, 获取验证码:{}", mobile, code);

        // 往redis中存放验证码，设置过期时间为十分钟
        jedisPoolUtil.set(mobile, code);
        jedisPoolUtil.expire(mobile, CODE_EXPIRE_TIME);

        // 记录手机号重复获取验证码的时间，间隔为一分钟
        jedisPoolUtil.set(keyMobile, code);
        jedisPoolUtil.expire(keyMobile, CODE_DATE_OUT_VALUE);


        // 给手机号发送短信验证码
//        try {
//            smsUtil.sendCode(mobile, code);
//        } catch (ClientException e) {
//            log.info("手机号:{}, 发送短信验证码失败，短信服务器异常！", mobile);
//            return new AiSportsResponse<Boolean>().fail().message("发送短信验证码失败！短信服务器异常！");
//        }

        return new AiSportsResponse<Boolean>().success().data(Boolean.TRUE);
    }

    @Override
    public AiSportsResponse<UserVo> info() {
        return new AiSportsResponse<UserVo>().success().data(userConverter.domain2Vo(getUser()));
    }

    @Override
    public AiSportsResponse<Boolean> check() {
        return new AiSportsResponse<Boolean>().success().data(Boolean.TRUE);
    }

    @Override
    @Log("刷新token信息")
    public AiSportsResponse<String> refresh() {
        UserSimple userSimple = getCurrentUser();

        // 生成token
        String token = JwtTokenUtil.generateToken(userSimple);
        return new AiSportsResponse<String>().success().data(token);
    }

    @Override
    @Log("用户上传个人头像")
    public AiSportsResponse<String> upload(@NotNull MultipartFile file) {
        try {

            File image = FileUtil.multipartFileToFile(file);
            String fileType = FileUtil.getFileType(image);

            fileType = StringUtils.lowerCase(fileType);
            if (!ArrayUtils.contains(AiSportsConstant.IMAGE_FILE_TYPE, fileType)) {
                return new AiSportsResponse<String>().message("上传的图片格式不正确！只能为" + Arrays.toString(IMAGE_FILE_TYPE)).fail();
            }

            // 上传图片, 返回OSS的图片路径
            String ossUrl = ossUploadUtil.uploadToOss(image);
            if (StringUtils.isEmpty(ossUrl)) {
                return new AiSportsResponse<String>().message("头像上传失败,请稍后再试！").fail();
            }
            User user = getUser();
            // 更新用户头像地址
            user.setAvatar(ossUrl);
            user.setModifyTime(new Date());
            userService.saveOrUpdate(user);
            return new AiSportsResponse<String>().success().data(ossUrl);
        } catch (Exception e) {
            String message = "头像上传失败,请稍后再试！";
            log.error(message, e);
            return new AiSportsResponse<String>().message(message).fail();
        }
    }

    @Override
    @Log("系统注册老师用户")
    public AiSportsResponse<Boolean> systemRegisterTeacher(@NotBlank @Pattern(regexp = AccountValidatorUtil.REGEX_MOBILE, message = "格式不正确") @RequestParam("mobile") String mobile,
                                                           @NotBlank @RequestParam("fullName") String fullName) {

        TeacherRegister register = userService.findTeacherRegisterByUsername(mobile);
        if (register != null) {
            return new AiSportsResponse<Boolean>().message("用户已经为注册老师，不需要重复注册。").fail();
        }

        return new AiSportsResponse<Boolean>().success().data(userService.registerTeacher(mobile, fullName));
    }
}
