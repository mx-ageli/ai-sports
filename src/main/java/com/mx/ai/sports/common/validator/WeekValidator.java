package com.mx.ai.sports.common.validator;

import com.mx.ai.sports.common.annotation.IsWeek;
import com.mx.ai.sports.common.entity.AiSportsConstant;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

/**
 * 校验是否为合法的星期格式
 * 星期，分别用 1周日 2周一 3周二 4周三 5周四 6周五 7周六 ... 周天7 格式： 以逗号拼接 1,2,3,4,5,6,7
 *
 * @author Mengjiaxin
 * @date 2019-08-20 16:28
 */
public class WeekValidator implements ConstraintValidator<IsWeek, String> {

    @Override
    public void initialize(IsWeek isWeek) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        String[] weeks = StringUtils.split(value, AiSportsConstant.SPLIT);
        // 固定的星期格式
        List<String> constWeekList = Arrays.asList(AiSportsConstant.WEEK);
        if (weeks == null || weeks.length == 0) {
            return false;
        } else {
            for (String week : weeks) {
                if (!constWeekList.contains(week)) {
                    return false;
                }
            }
        }
        return true;
    }
}
