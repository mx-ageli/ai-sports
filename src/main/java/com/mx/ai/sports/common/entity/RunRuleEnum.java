package com.mx.ai.sports.common.entity;

/**
 * 跑步规则类型： 1大于等于多少公里，2大于等于的时长
 * @author Mengjiaxin
 * @date 2020/8/5 4:06 下午
 */
public enum RunRuleEnum {
    /**
     * 1大于等于多少公里
     */
    MILEAGE(1L, "大于等于多少公里"),

    /**
     * 2大于等于的时长
     */
    RUN_TIME(2L, "大于等于的时长");

    private final Long value;

    private final String reasonPhrase;


    RunRuleEnum(Long value, String reasonPhrase) {
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
