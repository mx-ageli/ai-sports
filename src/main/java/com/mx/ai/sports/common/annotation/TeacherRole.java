package com.mx.ai.sports.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义的权限校验，默认为1学生 2老师
 * @author Mengjiaxin
 * @date 2020/8/17 11:41 下午
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TeacherRole {


}
