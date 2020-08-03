package com.mx.ai.sports.common.utils;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.mx.ai.sports.user.dto.UserSimple;
import com.mx.ai.sports.user.entity.User;
import com.mx.ai.sports.common.entity.AiSportsConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * EKB工具类
 *
 * @author Mengjiaxin
 * @date 2019-08-20 16:25
 */
@Slf4j
public class AiSportsUtil {

    /**
     * 驼峰转下划线
     *
     * @param value 待转换值
     * @return 结果
     */
    public static String camelToUnderscore(String value) {
        if (StringUtils.isBlank(value)) {
            return value;
        }
        String[] arr = StringUtils.splitByCharacterTypeCamelCase(value);
        if (arr.length == 0) {
            return value;
        }
        StringBuilder result = new StringBuilder();
        IntStream.range(0, arr.length).forEach(i -> {
            if (i != arr.length - 1) {
                result.append(arr[i]).append(StringPool.UNDERSCORE);
            } else {
                result.append(arr[i]);
            }
        });
        return StringUtils.lowerCase(result.toString());
    }

    /**
     * 下划线转驼峰
     *
     * @param value 待转换值
     * @return 结果
     */
    public static String underscoreToCamel(String value) {
        StringBuilder result = new StringBuilder();
        String[] arr = value.split("_");
        for (String s : arr) {
            result.append((String.valueOf(s.charAt(0))).toUpperCase()).append(s.substring(1));
        }
        return result.toString();
    }

    /**
     * 判断是否为 ajax请求
     *
     * @param request HttpServletRequest
     * @return boolean
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        return (request.getHeader("X-Requested-With") != null
                && "XMLHttpRequest".equals(request.getHeader("X-Requested-With")));
    }

    /**
     * 正则校验
     *
     * @param regex 正则表达式字符串
     * @param value 要匹配的字符串
     * @return 正则校验结果
     */
    public static boolean match(String regex, String value) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    /**
     * 获取当前用户的基础信息
     * @return
     */
    public static UserSimple getCurrentUser() {
        Object objectUser = SecurityUtils.getSubject().getPrincipal();
        if (objectUser != null) {
            UserSimple userSimple = new UserSimple();
            User user = (User) objectUser;

            userSimple.setUserId(user.getUserId());
            userSimple.setDeptId(user.getDeptId());
            userSimple.setNickname(user.getNickname());
            userSimple.setUsername(user.getUsername());
            userSimple.setRoleId(user.getRoleId());
            return userSimple;
        }

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(servletRequestAttributes).getRequest();

        final String requestHeader = request.getHeader(AiSportsConstant.AUTH_HEADER);

        if(StringUtils.isBlank(requestHeader)){
            return null;
        }

        boolean isExpired = JwtTokenUtil.isTokenExpired(requestHeader);
        if(isExpired){
            return null;
        }

        String simple = JwtTokenUtil.getUsernameFromToken(requestHeader);

        return JSON.parseObject(simple, UserSimple.class);
    }

    /**
     * 获取当前用户的Id
     * @return
     */
    public static Long getCurrentUserId(){
        return getCurrentUser().getUserId();
    }


    /**
     * 获取随机字符,自定义长度
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
