package com.mx.ai.sports.app.api;

import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.common.utils.AccountValidatorUtil;
import com.mx.ai.sports.system.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
     * @param deviceId 设备Id
     * @return token
     */
    @ApiOperation(value = "#已实现 2020-08-04# 手机号和短信验证码登录获得token,如果账号没有注册则会在第一次登录时自动注册。<接口限流：一分钟内只能访问3次>")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "query", dataType = "String", required = true),
            @ApiImplicitParam(name = "code", value = "短信验证码", paramType = "query", dataType = "String", required = true),
            @ApiImplicitParam(name = "deviceId", value = "设备Id，为极光推送的设备Id（现在可以随便传个值）", paramType = "query", dataType = "String", required = true)
    })
    @RequestMapping(value = "/v/login", method = RequestMethod.POST)
    AiSportsResponse<String> login(@NotBlank @Pattern(regexp = AccountValidatorUtil.REGEX_MOBILE, message = "格式不正确") String mobile,
                                   @NotBlank @Length(min = 6, max = 6, message = "长度必须等于6位") String code,
                                   @RequestParam(value = "deviceId") String deviceId) throws AiSportsException;

    /**
     * 获取手机验证码
     *
     * @param mobile 手机号
     * @return 是否成功发送验证码
     */
    @ApiOperation(value = "#已实现 2020-08-04# 获取手机验证码,通过手机号获得验证码(6位随机数). 在规定时间(60s)内重复获取验证码接口返回false! 测试时不发短信，调用该接口后默认使用666666 <接口限流：一分钟内只能访问3次>", notes = "获取手机验证码,通过手机号获得验证码(6位随机数). 在规定时间(60s)内重复获取验证码接口返回false!")
    @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "query", dataType = "String", required = true)
    @RequestMapping(value = "/v/get_code", method = RequestMethod.GET)
    AiSportsResponse<Boolean> getCode(@NotBlank @Pattern(regexp = AccountValidatorUtil.REGEX_MOBILE, message = "格式不正确") String mobile) throws AiSportsException;

    ;

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @ApiOperation(value = "#已实现 2020-08-04# 获取用户信息")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    AiSportsResponse<User> info();

    /**
     * 校验token是否有效
     *
     * @return 是否有效
     */
    @ApiOperation(value = "#已实现 2020-08-04# 校验token是否有效，只会返回true有效，false无效，不做其他功能")
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    AiSportsResponse<Boolean> check();


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
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    AiSportsResponse<String> upload(@NotNull MultipartFile file);

}
