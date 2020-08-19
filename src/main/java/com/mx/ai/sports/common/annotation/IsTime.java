package com.mx.ai.sports.common.annotation;

import com.mx.ai.sports.common.validator.TimeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 时间格式校验
 * @author Mengjiaxin
 * @date 2020/8/19 9:03 上午
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TimeValidator.class)
public @interface IsTime {

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
