package com.mx.ai.sports.app.controller;

import com.aliyuncs.exceptions.ClientException;
import com.mx.ai.sports.app.api.UserApi;
import com.mx.ai.sports.common.annotation.Limit;
import com.mx.ai.sports.common.annotation.Log;
import com.mx.ai.sports.common.controller.BaseRestController;
import com.mx.ai.sports.common.entity.*;
import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.common.oss.OssUploadUtil;
import com.mx.ai.sports.common.utils.*;
import com.mx.ai.sports.monitor.entity.LoginLog;
import com.mx.ai.sports.monitor.service.ILoginLogService;
import com.mx.ai.sports.system.converter.UserConverter;
import com.mx.ai.sports.system.entity.Classes;
import com.mx.ai.sports.system.entity.TeacherRegister;
import com.mx.ai.sports.system.entity.TempStudent;
import com.mx.ai.sports.system.entity.User;
import com.mx.ai.sports.system.query.UserCountQuery;
import com.mx.ai.sports.system.service.IClassesService;
import com.mx.ai.sports.system.service.ITempStudentService;
import com.mx.ai.sports.system.service.IUserService;
import com.mx.ai.sports.system.vo.TempStudentVo;
import com.mx.ai.sports.system.vo.UserCountVo;
import com.mx.ai.sports.system.vo.UserSimple;
import com.mx.ai.sports.system.query.UserUpdateVo;
import com.mx.ai.sports.system.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import wiki.xsx.core.util.RedisUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
    private ILoginLogService loginLogService;

    @Autowired
    private IUserService userService;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private SmsUtil smsUtil;

    @Autowired
    private OssUploadUtil ossUploadUtil;

    @Autowired
    private IClassesService classesService;

    @Autowired
    private ITempStudentService tempStudentService;

    public final static List<String> TEST_MOBILE = Arrays.asList("13708075380", "13036662958");

    /**
     * 通用验证码
     */
    private final static String COMMON_CODE = "075380";

    @Override
    @Log("手机号和短信验证码登录获得token")
    @Limit(key = "mobileLogin", period = 10, count = 3, name = "登录", prefix = "limit")
    public AiSportsResponse<String> login(@NotBlank @Pattern(regexp = AccountValidatorUtil.REGEX_MOBILE, message = "格式不正确") @RequestParam("mobile") String mobile,
                                          @NotBlank @Length(min = 6, max = 6, message = "长度必须等于6位") @RequestParam("code") String code) throws AiSportsException {
        boolean isMobile = AccountValidatorUtil.isMobile(mobile);
        if (!isMobile) {
            return new AiSportsResponse<String>().fail().message("手机号格式不正确!");
        }

        // 根据phone从redis中取出发送的短信验证码，并与用户输入的验证码比较
        String messageCode = RedisUtil.getStringHandler().get(mobile);

        // 如果是上架的测试手机号默认使用666666
        if (TEST_MOBILE.contains(mobile)) {
            messageCode = "666666";
        }
        if (COMMON_CODE.equals(code)) {
            messageCode = COMMON_CODE;
        }

        if (StringUtils.isEmpty(messageCode)) {
            return new AiSportsResponse<String>().fail().message("验证码过期,请重新获取!");
        } else if (!messageCode.equals(code)) {
            return new AiSportsResponse<String>().fail().message("验证码错误!");
        } else {
            // 检查用户是否已经注册，如果没有注册默认系统注册
            User user = userService.checkUserAndRegister(mobile);
            // 保存登录日志
            mobileLoginLog(mobile);
            // 生成token
            String token = JwtTokenUtil.generateToken(userConverter.domain2Simple(user));

            User updateUser = userService.getById(user.getUserId());

            HttpServletRequest request = getHttpServletRequest();
            // 从头信息里面获取到设备Id和类型
            final String deviceId = request.getHeader(DEVICE_ID);
            final String deviceType = request.getHeader(DEVICE_TYPE);
            // 如果传入有设备Id，将设备Id更新保存
            if (!StringUtils.isEmpty(deviceId) || !StringUtils.isEmpty(deviceType)) {
                updateUser.setDeviceId(deviceId);
                updateUser.setDeviceType(deviceType);
            }
            updateUser.setLastLoginTime(new Date());
            userService.saveOrUpdate(updateUser);

            // 登录成功后,删除验证码
            RedisUtil.getStringHandler().remove(mobile);

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
//    @Log("获取手机验证码")
    @Limit(key = "getCode", period = 10, count = 3, name = "获取手机验证码", prefix = "limit", limitType = LimitType.IP)
    public AiSportsResponse<Boolean> getCode(@NotBlank @Pattern(regexp = AccountValidatorUtil.REGEX_MOBILE, message = "格式不正确") @RequestParam("mobile") String mobile) throws AiSportsException {
        boolean isMobile = AccountValidatorUtil.isMobile(mobile);
        if (!isMobile) {
            return new AiSportsResponse<Boolean>().fail().message("手机号格式不正确!");
        }
        String keyMobile = CODE_DATE_OUT + mobile;
        // 先判断key是否存在，不存在则直接发送短信
        String isExistCode = RedisUtil.getStringHandler().get(keyMobile);
        if (StringUtils.isNotBlank(isExistCode)) {
            // key存在，获取过期时间
            Long ttlMobile = RedisUtil.getKeyHandler().getExpire(keyMobile, TimeUnit.SECONDS);
            if (ttlMobile > 0) {
                log.info("手机号:{}, 正在重复获取验证码, 间隔时间: {} 秒, 请求默认拒绝!", mobile, ttlMobile);
                String msg = "获取验证码太过频繁, 请等待" + ttlMobile + "秒后再次获取！";
                return new AiSportsResponse<Boolean>().message(msg).fail().data(Boolean.FALSE);
            }
        }

        String code = JwtTokenUtil.getRandomCode(mobile);

        log.info("手机号:{}, 获取验证码:{}", mobile, code);

        // 使用事物来提交
        // 往redis中存放验证码，设置过期时间为五分钟
        RedisUtil.getTransactionHandler().execute(handler -> {
            handler.beginTransaction();

            handler.getStringHandler().set(mobile, code);
            handler.getKeyHandler().expire(mobile, CODE_EXPIRE_TIME, TimeUnit.SECONDS);

            handler.getStringHandler().set(keyMobile, code);
            handler.getKeyHandler().expire(keyMobile, CODE_DATE_OUT_VALUE, TimeUnit.SECONDS);

            return handler.commit();
        });

        // 只有在非正式环境才发送短信
        if (ActiveProfileConstant.PROD.equals(SpringContextUtil.getActiveProfile())) {
            // 给手机号发送短信验证码
            try {
                smsUtil.sendCode(mobile, code);
            } catch (ClientException e) {
                log.info("手机号:{}, 发送短信验证码失败，短信服务器异常！", mobile);
                return new AiSportsResponse<Boolean>().fail().message("发送短信验证码失败！短信服务器异常！");
            }
        }

        return new AiSportsResponse<Boolean>().success().data(Boolean.TRUE);
    }

    @Override
    public AiSportsResponse<UserVo> info() {

        return new AiSportsResponse<UserVo>().success().data(userService.findVoById(getCurrentUserId()));
    }

    @Override
//    @Log("刷新token信息")
    public AiSportsResponse<String> refresh() {
        UserSimple userSimple = getCurrentUser();

        // 生成token
        String token = JwtTokenUtil.generateToken(userSimple);

        User user = userService.getById(userSimple.getUserId());
        // 更新最后一次使用时间
        user.setLastLoginTime(new Date());
        userService.updateById(user);

        return new AiSportsResponse<String>().success().data(token);
    }

    @Override
    @Log("用户上传个人头像")
    public AiSportsResponse<String> uploadAvatar(@NotNull MultipartFile file) {
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
                                                           @NotBlank @RequestParam("fullName") String fullName,
                                                           @NotNull @RequestParam("schoolId") Long schoolId) {

        TeacherRegister register = userService.findTeacherRegisterByUsername(mobile);
        if (register != null) {
            return new AiSportsResponse<Boolean>().message("用户已经为注册老师，不需要重复注册。").fail();
        }

        return new AiSportsResponse<Boolean>().success().data(userService.registerTeacher(mobile, fullName, schoolId));
    }

    @Override
    @Log("更新个人信息")
    public AiSportsResponse<Boolean> update(@RequestBody @Valid UserUpdateVo userUpdateVo) {
        User user = getUser();
        // 只能学生才能调用这个接口进行修改
//        if (!Objects.equals(RoleEnum.STUDENT.value(), user.getRoleId())) {
//            return new AiSportsResponse<Boolean>().message("当前能用户不是一个学生，不能修改！").fail();
//        } else {
        if (StringUtils.isBlank(user.getFullName()) || StringUtils.isBlank(user.getSno())) {
            return new AiSportsResponse<Boolean>().message("还没有绑定初始个人信息，不能修改！").fail();
        }

        if (StringUtils.isNotBlank(userUpdateVo.getSno())) {
            // 通过学号去查询
            User snoUser = userService.findBySno(userUpdateVo.getSno());
            // 校验这个学号是否存在，而且判断是否有别的学生在使用
            if (snoUser != null && !Objects.equals(snoUser.getUserId(), user.getUserId())) {
                return new AiSportsResponse<Boolean>().message("学号错误，学号已经被其他同学使用！").fail();
            }
            user.setSno(userUpdateVo.getSno());
        }
        if (StringUtils.isNotBlank(userUpdateVo.getFullName())) {
            user.setFullName(userUpdateVo.getFullName());
        }
        if (null != userUpdateVo.getClassesId()) {
            Classes classes = classesService.getById(userUpdateVo.getClassesId());
            if (classes == null) {
                return new AiSportsResponse<Boolean>().message("传入的班级Id错误！没有查询到相应的班级数据！").fail();
            }
            // 添加班级Id
            user.setClassesId(classes.getClassesId());
        }
        if (null != userUpdateVo.getSchoolId()) {
            user.setSchoolId(userUpdateVo.getSchoolId());
        }
        if (StringUtils.isNotBlank(userUpdateVo.getSex())) {

            if (Objects.equals(userUpdateVo.getSex(), SexEnum.MALE.value()) || Objects.equals(userUpdateVo.getSex(), SexEnum.FEMALE.value())) {
                user.setSex(userUpdateVo.getSex());
            } else {
                return new AiSportsResponse<Boolean>().message("性别只能为 (1或者2) 1男 2女").fail();
            }
        }
        // 重置修改时间
        user.setModifyTime(new Date());
        return new AiSportsResponse<Boolean>().success().data(userService.updateById(user));
//        }
    }

    @Override
    @Log("查询学生的临时信息")
    public AiSportsResponse<TempStudentVo> findTempStudentInfo(@NotBlank @RequestParam("fullName") String fullName, @NotBlank @RequestParam("sno") String sno) {

        TempStudentVo tempStudentVo = tempStudentService.findTempStudentInfo(fullName, sno);
        if (tempStudentVo == null) {
            return new AiSportsResponse<TempStudentVo>().fail().message("没有查询到匹配信息！");
        }
        return new AiSportsResponse<TempStudentVo>().success().data(tempStudentVo);
    }

    @Override
    @Log("绑定手机号与学生临时信息的关系")
    public AiSportsResponse<Boolean> bindStudentInfo(@NotNull @RequestParam("tempStudentId") Long tempStudentId) {
        // 先查询到临时信息
        TempStudent tempStudent = tempStudentService.getById(tempStudentId);
        if (tempStudent == null) {
            return new AiSportsResponse<Boolean>().fail().message("临时学生Id有误，没有查询到数据！");
        }
        if (tempStudent.getIsRegister()) {
            return new AiSportsResponse<Boolean>().fail().message("当前学生已经绑定了基础信息，不能重复绑定！");
        }
        // 查询学号是否被别人已经使用
        User snoUser = userService.findBySno(tempStudent.getSno());
        if (snoUser != null) {
            return new AiSportsResponse<Boolean>().fail().message("当前学号已经被其他同学所绑定，不能重复绑定！请联系管理员！");
        }

        User user = getUser();
        if (user == null) {
            return new AiSportsResponse<Boolean>().fail().message("获取用户信息异常！");
        }
        if (StringUtils.isNotBlank(user.getFullName())) {
            return new AiSportsResponse<Boolean>().fail().message("当前手机号已经绑定了基础信息，不能重复绑定！");
        }
        // 绑定基础信息
        return new AiSportsResponse<Boolean>().success().data(tempStudentService.bind(tempStudent, user));
    }

    @Override
    public AiSportsResponse<List<UserCountVo>> findActiveUserCount(@RequestBody @Valid UserCountQuery query) {

        return new AiSportsResponse<List<UserCountVo>>().success().data(userService.findActiveUserCount(query.getStartTime(), query.getEndTime()));
    }

    @Override
    public AiSportsResponse<List<UserCountVo>> findAddUserCount(@RequestBody @Valid UserCountQuery query) {

        return new AiSportsResponse<List<UserCountVo>>().success().data(userService.findAddUserCount(query.getStartTime(), query.getEndTime()));
    }

    @Override
    public AiSportsResponse<Boolean> changeUsername(@RequestParam("oldPhone") String oldPhone, @RequestParam("newPhone") String newPhone) {

        User user = userService.findByUsername(newPhone);
        if (user != null) {

            if (StringUtils.isNotBlank(user.getFullName())) {
                return new AiSportsResponse<Boolean>().fail().message("新手机号已经被" + user.getFullName() + "(学号:" + user.getSno() + ")绑定过，不能执行绑定操作");
            }
            // 把新手机号对应的账号先删除
            userService.removeById(user.getUserId());
        }

        User updateUser = userService.findByUsername(oldPhone);
        if(updateUser == null){
            return new AiSportsResponse<Boolean>().fail().message("老手机号不存在，" + oldPhone);
        }
        updateUser.setUsername(newPhone);

        return new AiSportsResponse<Boolean>().data(userService.updateById(updateUser)).message("修改成功！");
    }
}
