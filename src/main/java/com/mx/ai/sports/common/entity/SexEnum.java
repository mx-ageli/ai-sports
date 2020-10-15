package com.mx.ai.sports.common.entity;

/**
 * 性别 1男 2女
 *
 * @author Mengjiaxin
 * @date 2020/8/5 4:06 下午
 */
public enum SexEnum {

    MALE("1", "男"),

    FEMALE("2", "女");

    private final String value;

    private final String reasonPhrase;


    SexEnum(String value, String reasonPhrase) {
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
