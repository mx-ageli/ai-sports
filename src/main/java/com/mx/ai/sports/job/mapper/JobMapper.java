package com.mx.ai.sports.job.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mx.ai.sports.job.entity.Job;

import java.util.List;

/**
 *
 * @author Mengjiaxin
 * @date 2020/8/17 7:17 下午
 */
public interface JobMapper extends BaseMapper<Job> {
	
	List<Job> queryList();
}