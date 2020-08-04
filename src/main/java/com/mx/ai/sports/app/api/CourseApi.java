package com.mx.ai.sports.app.api;

import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 课程相关接口
 * @author Mengjiaxin
 * @date 2020/8/3 2:24 下午
 */
@Validated
@Api(tags = "50-课程相关接口", protocols = "application/json")
@RequestMapping(value = "/api/course", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface CourseApi {


}
