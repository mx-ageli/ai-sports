package com.mx.ai.sports.common.entity;

/**
 * 1打卡状态：1正常 2迟到 3缺席 4缺卡
 *
 * @author Mengjiaxin
 * @date 2020/8/5 4:06 下午
 */
public enum SignedStatusEnum {
    /**
     * 打卡状态：1正常 2迟到 3缺席 4缺卡
     */
    NORMAL("1", "正常"),

    LATE("2", "迟到"),

    ABSENT("3", "缺席"),

    NO_SIGNED("4", "缺卡");

    private final String value;

    private final String reasonPhrase;


    SignedStatusEnum(String value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public String value() {
        return this.value;
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }


}
