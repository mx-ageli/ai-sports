package com.mx.ai.sports.common.entity;

/**
 * 课程报名状态，1可报课 2不可报 3已报课
 * @author Mengjiaxin
 * @date 2020/8/5 4:06 下午
 */
public enum EntryEnum {

    OK("1", "可报课"),

    NO("2", "不可报"),

    ENTRY("3", "已报课"),

    FINISH("4", "已结束");

    private final String value;

    private final String reasonPhrase;


    EntryEnum(String value, String reasonPhrase) {
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
