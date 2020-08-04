package com.mx.ai.sports.app.controller;

import com.mx.ai.sports.app.api.ClassesApi;
import com.mx.ai.sports.common.controller.BaseRestController;
import com.mx.ai.sports.common.entity.AiSportsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


/**
 * 班级相关接口
 * @author Mengjiaxin
 * @date 2020/8/3 2:34 下午
 */
@Slf4j
@RestController("ClassesApi")
public class ClassesController extends BaseRestController implements ClassesApi {


    @Override
    public AiSportsResponse<Boolean> add() {
        return null;
    }

    @Override
    public AiSportsResponse<Boolean> update() {
        return null;
    }

    @Override
    public AiSportsResponse<Boolean> list() {
        return null;
    }

}
