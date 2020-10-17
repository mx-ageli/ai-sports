package com.mx.ai.sports.common.entity;

/**
 * 跑步类型
 * @author Mengjiaxin
 * @date 2020/10/17 10:45 下午
 */
public enum RunTypeEnum {
    /**
     * 1 跑步课程
     */
    RUN(1L, "跑步课程"),

    /**
     * 2 健身课程
     */
    SPORT(2L, "健身课程");

    private final Long value;

    private final String reasonPhrase;


    RunTypeEnum(Long value, String reasonPhrase) {
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
