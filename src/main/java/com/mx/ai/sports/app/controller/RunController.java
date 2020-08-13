package com.mx.ai.sports.app.controller;

import com.mx.ai.sports.app.api.RunApi;
import com.mx.ai.sports.common.controller.BaseRestController;
import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.course.entity.Run;
import com.mx.ai.sports.course.entity.RunRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


/**
 * 跑步相关接口
 *
 * @author Mengjiaxin
 * @date 2019-08-28 16:04
 */
@Slf4j
@RestController("RunApi")
public class RunController extends BaseRestController implements RunApi {

    @Override
    public AiSportsResponse<Boolean> add(@RequestBody @Valid Run run) {
        return null;
    }

    @Override
    public AiSportsResponse<List<RunRule>> findRunRule() {
        return null;
    }

    @Override
    public AiSportsResponse<List<Object>> findRunHistory() {
        return null;
    }

}
