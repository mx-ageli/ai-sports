package com.mx.ai.sports.app.controller;

import com.mx.ai.sports.app.api.ReplaceApi;
import com.mx.ai.sports.common.controller.BaseRestController;
import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.common.entity.ReplaceTypeEnum;
import com.mx.ai.sports.system.service.IReplaceService;
import com.mx.ai.sports.system.vo.ReplaceVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.Objects;


/**
 * 系统版本更新
 *
 * @author Mengjiaxin
 * @date 2020/9/9 2:40 下午
 */
@Slf4j
@RestController("ReplaceApi")
public class ReplaceController extends BaseRestController implements ReplaceApi {

    @Autowired
    private IReplaceService replaceService;

    @Override
    public AiSportsResponse<ReplaceVo> findByLast(@NotNull @RequestParam("type") String type) {
        if(!Objects.equals(type, ReplaceTypeEnum.IOS.value()) && !Objects.equals(type, ReplaceTypeEnum.ANDROID.value())){
            return new AiSportsResponse<ReplaceVo>().fail().message("设备类型 只能是 1：iOS 2：Android");
        }
        return new AiSportsResponse<ReplaceVo>().success().data(replaceService.findByLast(type));
    }

}
