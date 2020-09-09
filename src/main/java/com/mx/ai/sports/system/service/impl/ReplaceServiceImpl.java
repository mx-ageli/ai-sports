package com.mx.ai.sports.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mx.ai.sports.system.converter.ReplaceConverter;
import com.mx.ai.sports.system.entity.Replace;
import com.mx.ai.sports.system.mapper.ReplaceMapper;
import com.mx.ai.sports.system.service.IReplaceService;
import com.mx.ai.sports.system.vo.ReplaceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * 系统更新Service
 *
 * @author Mengjiaxin
 * @date 2019-08-20 19:58
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ReplaceServiceImpl extends ServiceImpl<ReplaceMapper, Replace> implements IReplaceService {

    @Autowired
    private ReplaceConverter replaceConverter;

    @Override
    public ReplaceVo findByLast(String type) {

        Replace replace = this.baseMapper.selectOne(new LambdaQueryWrapper<Replace>().eq(Replace::getType, type).orderByDesc(Replace::getReleaseTime).last("LIMIT 1"));

        return replaceConverter.domain2Vo(replace);
    }
}
