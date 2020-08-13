package com.mx.ai.sports.common.entity;

/**
 * 跑步状态： 1合格 2不合格
 * @author Mengjiaxin
 * @date 2020/8/5 4:06 下午
 */
public enum RunStatusEnum {
    /**
     * 1合格
     */
    PASS(1L, "合格"),

    /**
     * 2不合格
     */
    NO_PASS(2L, "不合格");

    private final Long value;

    private final String reasonPhrase;


    RunStatusEnum(Long value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public Long value() {
        return this.value;
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }


}
