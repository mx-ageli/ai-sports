package com.mx.ai.sports.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mx.ai.sports.system.entity.Term;
import com.mx.ai.sports.system.vo.TermVo;

import java.util.List;

/**
 * 学期Term
 *
 * @author Mengjiaxin
 * @date 2020/8/6 10:39 上午
 */
public interface ITermService extends IService<Term> {

    /**
     * 根据当前时间查询学期，当前和过去
     * @return
     */
    List<TermVo> findByCurrentDate();

}
