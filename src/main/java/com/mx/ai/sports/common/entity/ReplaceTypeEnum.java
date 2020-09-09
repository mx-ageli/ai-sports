package com.mx.ai.sports.common.entity;

/**
 * 系统更新设备类型 1iOS 2Android
 *
 * @author Mengjiaxin
 * @date 2020/9/8 7:04 下午
 */
public enum ReplaceTypeEnum {

    IOS("1", "iOS"),

    ANDROID("2", "Android");

    private final String value;

    private final String reasonPhrase;


    ReplaceTypeEnum(String value, String reasonPhrase) {
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
