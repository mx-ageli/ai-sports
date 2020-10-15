package com.mx.ai.sports.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mx.ai.sports.system.entity.TempStudent;
import com.mx.ai.sports.system.vo.TempStudentVo;

/**
 * 导入的学生临时信息Mapper
 * @author Mengjiaxin
 * @date 2020/10/14 5:22 下午
 */
public interface TempStudentMapper extends BaseMapper<TempStudent> {

    TempStudentVo findTempStudentInfo(String fullName, String sno);

}
