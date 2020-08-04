package com.mx.ai.sports.app.controller;

import com.mx.ai.sports.app.api.TermApi;
import com.mx.ai.sports.common.controller.BaseRestController;
import com.mx.ai.sports.common.entity.AiSportsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


/**
 * 学期相关接口
 *
 * @author Mengjiaxin
 * @date 2019-08-28 16:04
 */
@Slf4j
@RestController("TermApi")
public class TermController extends BaseRestController implements TermApi {

    @Override
    public AiSportsResponse<Boolean> list() {
        return null;
    }

}
