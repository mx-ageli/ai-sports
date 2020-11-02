package com.mx.ai.sports.course.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mx.ai.sports.course.entity.Keep;
import com.mx.ai.sports.course.vo.CountVo;
import com.mx.ai.sports.course.vo.KeepRecordVo;
import com.mx.ai.sports.course.vo.RunRecordDetailVo;

import java.util.List;

/**
 *
 * @author Mengjiaxin
 * @date 2020/8/24 5:22 下午
 */
public interface KeepMapper extends BaseMapper<Keep> {


    IPage<KeepRecordVo> findKeepRecordVo(Page<RunRecordDetailVo> page, Long userId, String status);

    List<CountVo> findCountByCourseRecordId(Long courseRecordId);
}