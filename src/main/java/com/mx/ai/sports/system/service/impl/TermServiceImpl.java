package com.mx.ai.sports.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mx.ai.sports.system.converter.TermConverter;
import com.mx.ai.sports.system.entity.Term;
import com.mx.ai.sports.system.mapper.TermMapper;
import com.mx.ai.sports.system.service.ITermService;
import com.mx.ai.sports.system.vo.TermVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 学期service
 * @author Mengjiaxin
 * @date 2020/8/6 10:39 上午
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TermServiceImpl extends ServiceImpl<TermMapper, Term> implements ITermService {

    @Autowired
    private TermConverter termConverter;

    @Override
    public List<TermVo> findByCurrentDate() {

        List<Term> termList = this.baseMapper.selectList(new LambdaQueryWrapper<Term>().le(Term::getBeginTime, new Date()));
        return termConverter.domain2Vos(termList);
    }
}
