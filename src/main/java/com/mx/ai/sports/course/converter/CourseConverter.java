package com.mx.ai.sports.course.converter;

import com.mx.ai.sports.course.entity.Course;
import com.mx.ai.sports.course.query.CourseUpdateVo;
import com.mx.ai.sports.system.entity.Classes;
import com.mx.ai.sports.system.vo.ClassesVo;
import org.mapstruct.Mapper;

import java.util.List;


/**
 * CourseVO转换类
 *
 * @author Mengjiaxin
 * @date 2019-09-05 15:17
 */
@Mapper(componentModel = "spring", uses = {})
public interface CourseConverter {


    /**
     * 转换为课程对象
     *
     * @param updateVo
     * @return
     */
    Course vo2Domain(CourseUpdateVo updateVo);


}
