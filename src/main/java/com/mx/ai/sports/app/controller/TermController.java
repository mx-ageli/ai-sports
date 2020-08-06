package com.mx.ai.sports.app.controller;

import com.mx.ai.sports.app.api.TermApi;
import com.mx.ai.sports.common.controller.BaseRestController;
import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.system.service.ITermService;
import com.mx.ai.sports.system.vo.TermVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 学期相关接口
 *
 * @author Mengjiaxin
 * @date 2019-08-28 16:04
 */
@Slf4j
@RestController("TermApi")
public class TermController extends BaseRestController implements TermApi {

    @Autowired
    private ITermService termService;

    @Override
    public AiSportsResponse<List<TermVo>> list() {

        return new AiSportsResponse<List<TermVo>>().success().data(termService.findByCurrentDate());
    }

}
