package com.mx.ai.sports.app.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mx.ai.sports.app.api.MessageApi;
import com.mx.ai.sports.common.controller.BaseRestController;
import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.common.entity.QueryRequest;
import com.mx.ai.sports.system.service.IMessageService;
import com.mx.ai.sports.system.vo.MessageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


/**
 * 系统消息
 * @author Mengjiaxin
 * @date 2020/9/8 6:15 下午
 */
@Slf4j
@RestController("MessageApi")
public class MessageController extends BaseRestController implements MessageApi {

    @Autowired
    private IMessageService messageService;

    @Override
    public AiSportsResponse<IPage<MessageVo>> findMyMessage(@RequestBody @Valid QueryRequest query) {
        return new AiSportsResponse<IPage<MessageVo>>().success().data(messageService.findMyMessage(query, getCurrentUserId()));
    }
}
