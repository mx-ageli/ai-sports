package com.mx.ai.sports.common.annotation;


import com.mx.ai.sports.common.validator.WeekValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 校验星期格式
 * @author Mengjiaxin
 * @date 2020/8/19 9:03 上午
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = WeekValidator.class)
public @interface IsWeek {

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
