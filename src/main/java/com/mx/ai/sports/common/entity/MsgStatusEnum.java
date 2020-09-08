package com.mx.ai.sports.common.entity;

/**
 * 系统消息状态 1已读 2未读
 * @author Mengjiaxin
 * @date 2020/9/8 7:04 下午
 */
public enum MsgStatusEnum {

    READ("1", "已读"),

    UNREAD("2", "未读");

    private final String value;

    private final String reasonPhrase;


    MsgStatusEnum(String value, String reasonPhrase) {
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
