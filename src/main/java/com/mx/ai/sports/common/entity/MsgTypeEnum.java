package com.mx.ai.sports.common.entity;

/**
 * 消息类型 1课程发布通知 2课程开始前的通知
 * @author Mengjiaxin
 * @date 2020/9/8 7:04 下午
 */
public enum MsgTypeEnum {

    COURSE_PUBLISH("1", "课程发布通知"),

    COURSE_START("2", "课程开始前的通知");

    private final String value;

    private final String reasonPhrase;


    MsgTypeEnum(String value, String reasonPhrase) {
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
