package com.mx.ai.sports.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mx.ai.sports.system.vo.ClassesVo;
import com.mx.ai.sports.system.entity.Classes;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 班级Mapper
 *
 * @author Mengjiaxin
 * @date 2020/8/4 11:19 上午
 */
public interface ClassesMapper extends BaseMapper<Classes> {

    /**
     * 根据学校Id查询班级信息
     *
     * @param schoolId
     * @return
     */
    List<ClassesVo> findBySchoolId(@Param("schoolId") Long schoolId);

    /**
     * 根据学校Id和创建人查询班级信息
     *
     * TODO 修改方法
     *
     * @param schoolId
     * @return
     */
    List<ClassesVo> findBySchoolIdAndUserId(@Param("schoolId") Long schoolId, @Param("userId") Long userId);

    /**
     *
     * @param classesId
     * @return
     */
    ClassesVo findById(Long classesId);
}
