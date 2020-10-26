package com.mx.ai.sports.app.api;

import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.common.utils.AccountValidatorUtil;
import com.mx.ai.sports.system.query.UserUpdateVo;
import com.mx.ai.sports.system.vo.TempStudentVo;
import com.mx.ai.sports.system.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 用户相关接口
 *
 * @author Mengjiaxin
 * @date 2020/7/14 4:40 下午
 */
@Validated
@Api(tags = "10-用户相关接口", protocols = "application/json")
@RequestMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface UserApi {

    /**
     * 手机号和短信验证码登录获得token
     *
     * @param mobile   手机号
     * @param code     验证码
     * @return token
     */
    @ApiOperation(value = "#已实现 图21# 手机号和短信验证码登录获得token,如果账号没有注册则会在第一次登录时自动注册。<接口限流：一分钟内只能访问3次>")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "query", dataType = "String", required = true),
            @ApiImplicitParam(name = "code", value = "短信验证码", paramType = "query", dataType = "String", required = true)
    })
    @RequestMapping(value = "/v/login", method = RequestMethod.POST)
    AiSportsResponse<String> login(@NotBlank @Pattern(regexp = AccountValidatorUtil.REGEX_MOBILE, message = "格式不正确") @RequestParam("mobile") String mobile,
                                   @NotBlank @Length(min = 6, max = 6, message = "长度必须等于6位") @RequestParam("code") String code) throws AiSportsException;

    /**
     * 获取手机验证码
     *
     * @param mobile 手机号
     * @return 是否成功发送验证码
     */
    @ApiOperation(value = "#已实现 图21# 获取手机验证码,通过手机号获得验证码(6位随机数). 在规定时间(60s)内重复获取验证码接口返回false! 测试时不发短信，调用该接口后默认使用666666 <接口限流：一分钟内只能访问3次>", notes = "获取手机验证码,通过手机号获得验证码(6位随机数). 在规定时间(60s)内重复获取验证码接口返回false!")
    @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "query", dataType = "String", required = true)
    @RequestMapping(value = "/v/get_code", method = RequestMethod.GET)
    AiSportsResponse<Boolean> getCode(@NotBlank @Pattern(regexp = AccountValidatorUtil.REGEX_MOBILE, message = "格式不正确") @RequestParam("mobile") String mobile) throws AiSportsException;

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @ApiOperation(value = "#已实现 2020-08-04# 获取用户信息，同时也可以用来校验token的正确性")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    AiSportsResponse<UserVo> info();

    /**
     * 刷新用户的token信息
     *
     * @return token
     */
    @ApiOperation(value = "#已实现 2020-08-04# 刷新用户的token信息，获取一个新的token，实现延长token过期时间")
    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    AiSportsResponse<String> refresh();

    /**
     * 用户修改自己的头像
     *
     * @param file 上传的文件
     * @return 上传成功后返回的oss地址
     */
    @ApiOperation(value = "#已实现 2020-08-04# 用户修改自己的头像 成功后返回头像地址")
    @RequestMapping(value = "/upload_avatar", method = RequestMethod.POST)
    AiSportsResponse<String> uploadAvatar(@NotNull MultipartFile file);

    /**
     * 将单个手机号设置为老师
     *
     * @param mobile   手机号
     * @param fullName 老师姓名
     * @return
     */
    @ApiOperation(value = "#已实现 2020-08-05# 将单个手机号设置为老师，这个接口只是后台用来添加老师，以后将支持批量导入老师")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "query", dataType = "String", required = true),
            @ApiImplicitParam(name = "fullName", value = "老师姓名", paramType = "query", dataType = "String", required = true),
            @ApiImplicitParam(name = "schoolId", value = "学校Id", paramType = "query", dataType = "Long", required = true)
    })
    @RequestMapping(value = "/v/system_register_teacher", method = RequestMethod.GET)
    AiSportsResponse<Boolean> systemRegisterTeacher(@NotBlank @Pattern(regexp = AccountValidatorUtil.REGEX_MOBILE, message = "格式不正确") @RequestParam("mobile") String mobile,
                                                    @NotBlank @RequestParam("fullName") String fullName,
                                                    @NotNull @RequestParam("schoolId") Long schoolId);

    /**
     * 更新个人信息，只能修改学号、姓名、班级、性别。都是非必填字段，各项传值才修改，不传的不修改。
     *
     * @param userUpdateVo 个人资料参数
     * @return
     */
    @ApiOperation(value = "#已实现 图22# 更新个人信息，只能修改学号、姓名、班级、性别。都是非必填字段，各项传值才修改，不传的不修改。")
    @ApiImplicitParam(name = "userUpdateVo", value = "个人资料参数", paramType = "body", dataType = "UserUpdateVo", required = true)
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    AiSportsResponse<Boolean> update(@RequestBody @Valid UserUpdateVo userUpdateVo);

    /**
     * 查询学生的临时信息
     *
     * @param fullName 老师姓名
     * @param sno      学号
     * @return
     */
    @ApiOperation(value = "#已实现 2020-10-15# 查询学生的临时信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fullName", value = "学生姓名", paramType = "query", dataType = "String", required = true),
            @ApiImplicitParam(name = "sno", value = "学号", paramType = "query", dataType = "String", required = true)
    })
    @RequestMapping(value = "/find_temp_student_info", method = RequestMethod.GET)
    AiSportsResponse<TempStudentVo> findTempStudentInfo(@NotBlank @RequestParam("fullName") String fullName, @NotBlank @RequestParam("sno") String sno);

    /**
     * 绑定手机号与学生临时信息的关系,初始化学生基础信息
     * @param tempStudentId
     * @return
     */
    @ApiOperation(value = "#已实现 2020-10-15# 绑定手机号与学生临时信息的关系,初始化学生基础信息")
    @ApiImplicitParam(name = "tempStudentId", value = "临时学生的Id， 通过find_temp_student_info获取", paramType = "query", dataType = "Long", required = true)
    @RequestMapping(value = "/bind_student_info", method = RequestMethod.GET)
    AiSportsResponse<Boolean> bindStudentInfo(@NotNull @RequestParam("tempStudentId") Long tempStudentId);


}
