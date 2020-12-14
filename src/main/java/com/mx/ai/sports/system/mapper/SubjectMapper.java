package com.mx.ai.sports.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mx.ai.sports.system.dto.SubjectTeacherDto;
import com.mx.ai.sports.system.entity.Subject;

import java.util.List;

/**
 * 主课Mapper
 * @author Mengjiaxin
 * @date 2020/10/14 5:22 下午
 */
public interface SubjectMapper extends BaseMapper<Subject> {

    List<SubjectTeacherDto> findSubjectTeacherDto();

}
