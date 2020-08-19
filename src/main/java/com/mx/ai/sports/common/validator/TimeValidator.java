package com.mx.ai.sports.common.validator;

import com.mx.ai.sports.common.annotation.IsTime;
import com.mx.ai.sports.common.utils.DateUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.SimpleDateFormat;
import java.time.LocalTime;

/**
 * 校验是否为合法的时间格式
 *
 * @author Mengjiaxin
 * @date 2019-08-20 16:28
 */
public class TimeValidator implements ConstraintValidator<IsTime, String> {

    @Override
    public void initialize(IsTime isTime) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.TIME_PATTERN);
            sdf.parse(value);
            LocalTime localTime = LocalTime.parse(value);
            return localTime != null;
        } catch (Exception e) {
            return false;
        }
    }
}
