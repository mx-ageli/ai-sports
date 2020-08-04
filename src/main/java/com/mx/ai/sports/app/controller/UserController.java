package com.mx.ai.sports.app.controller;

import com.aliyuncs.exceptions.ClientException;
import com.mx.ai.sports.app.api.UserApi;
import com.mx.ai.sports.common.annotation.Limit;
import com.mx.ai.sports.common.annotation.Log;
import com.mx.ai.sports.common.controller.BaseRestController;
import com.mx.ai.sports.common.entity.AiSportsConstant;
import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.common.oss.OssUploadUtil;
import com.mx.ai.sports.common.utils.*;
import com.mx.ai.sports.monitor.entity.LoginLog;
import com.mx.ai.sports.monitor.service.ILoginLogService;
import com.mx.ai.sports.system.converter.UserConverter;
import com.mx.ai.sports.system.dto.UserSimple;
import com.mx.ai.sports.system.entity.User;
import com.mx.ai.sports.system.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

import static com.mx.ai.sports.common.entity.AiSportsConstant.DEFAULT_AVATAR;
import static com.mx.ai.sports.common.entity.AiSportsConstant.DEFAULT_SCHOOL_ID;


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
    public AiSportsResponse<String> login(@NotBlank @Pattern(regexp = AccountValidatorUtil.REGEX_MOBILE, message = "格式不正确") String mobile,
                                          @NotBlank @Length(min = 6, max = 6, message = "长度必须等于6位") String code,
                                          @RequestParam(value = "deviceId") String deviceId) throws AiSportsException{
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
            // 先通过手机号查询用户信息
            User user = userService.findByUsername(mobile);
            // 用户还没有注册，系统给默认注册
            if(user == null){
                user = new User();
                user.setUsername(mobile);
                user.setCreateTime(new Date());
                user.setModifyTime(new Date());
                user.setLastLoginTime(new Date());
                user.setDeviceId(deviceId);
                user.setSchoolId(DEFAULT_SCHOOL_ID);
                user.setAvatar(DEFAULT_AVATAR);

                userService.save(user);
            }

            // 保存登录日志
            mobileLoginLog(mobile);

            // 生成token
            String token = JwtTokenUtil.generateToken(userConverter.domain2Simple(user));
            // 登录成功后,删除验证码
            jedisPoolUtil.del(mobile);
            // 如果传入有设备Id，将设备Id更新保存
            if (!StringUtils.isEmpty(deviceId)) {
                User updateUser = userService.getById(user.getUserId());
                updateUser.setDeviceId(deviceId);
                userService.saveOrUpdate(updateUser);
            }
            return new AiSportsResponse<String>().success().data(token);
        }
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
    @Limit(key = "getCode", period = 60, count = 3, name = "获取手机验证码", prefix = "limit")
    public AiSportsResponse<Boolean> getCode(@NotBlank @Pattern(regexp = AccountValidatorUtil.REGEX_MOBILE, message = "格式不正确") String mobile) throws AiSportsException {
        boolean isMobile = AccountValidatorUtil.isMobile(mobile);
        if (!isMobile) {
            return new AiSportsResponse<Boolean>().fail().message("手机号格式不正确!");
        }
        // 先判断key是否存在，不存在则直接发送短信
        Boolean isExists = jedisPoolUtil.exists(mobile);
        if (isExists) {
            // key存在，获取过期时间
            Long ttlMobile = jedisPoolUtil.ttl(mobile);
            log.info("手机号:{}, 正在重复获取验证码, 间隔时间: {} 秒, 请求默认拒绝!", mobile, ttlMobile);
            String msg = "获取验证码太过频繁, 请等待" + ttlMobile + "秒后再次获取！";
            return new AiSportsResponse<Boolean>().message(msg).fail().data(Boolean.FALSE);
        }

        String code = JwtTokenUtil.getRandomCode();
        code = "666666";
        log.info("手机号:{}, 获取验证码:{}", mobile, code);

        // 往redis中存放验证码
        jedisPoolUtil.set(mobile, code);
        jedisPoolUtil.expire(mobile, AiSportsConstant.CODE_DATE_OUT_VALUE);

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
    public AiSportsResponse<User> info() {
        return new AiSportsResponse<User>().success().data(getUser());
    }

    @Override
    @Log("校验token是否有效")
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
            // 上传图片, 返回OSS的图片路径
            String ossUrl = ossUploadUtil.uploadToOss(FileUtil.multipartFileToFile(file));
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
}
