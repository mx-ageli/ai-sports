package com.mx.ai.sports.common.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * 通用的返回值
 *
 * @author Mengjiaxin
 * @date 2019-08-20 16:16
 */
@Getter
@ApiModel("返回类")
public class AiSportsResponse<T> implements Serializable {

    private static final long serialVersionUID = -4078639268249337246L;

    @ApiModelProperty("code")
    private int code;

    @ApiModelProperty(value = "描述")
    private String message;

    @ApiModelProperty("结果")
    private T data;

    public AiSportsResponse<T> code(HttpStatus status) {
        this.code = status.value();
        return this;
    }

    public AiSportsResponse<T> message(String message) {
        this.message = message;
        return this;
    }

    public AiSportsResponse<T> data(T data) {
        this.data = data;
        return this;
    }

    public AiSportsResponse<T> success() {
        this.code(HttpStatus.OK);
        return this;
    }

    public AiSportsResponse<T> fail() {
        this.code(HttpStatus.INTERNAL_SERVER_ERROR);
        return this;
    }


}
