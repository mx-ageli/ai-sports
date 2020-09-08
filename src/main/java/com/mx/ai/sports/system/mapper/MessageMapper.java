package com.mx.ai.sports.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mx.ai.sports.system.entity.Message;
import com.mx.ai.sports.system.vo.MessageVo;

/**
 * 消息mapper
 * @author Mengjiaxin
 * @date 2020/9/8 6:27 下午
 */
public interface MessageMapper extends BaseMapper<Message> {

    IPage<MessageVo> findMyMessage(Page<MessageVo> page, Long userId);
}
