package com.mx.ai.sports.common.annotation;


import com.mx.ai.sports.common.validator.CronValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 校验是否为合法的 Cron表达式
 *
 * @author Mengjiaxin
 * @date 2020/8/17 7:19 下午
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CronValidator.class)
public @interface IsCron {

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}