package com.mx.ai.sports.common.oss;


/**
 * OSS服务类型
 *
 * @author Mengjiaxin
 * @date 2019-08-15
 */
public enum OssTypeEnum {
    
    /**
     * 七牛OSS
     */
    QI_NIU(0, "七牛OSS"),

    /**
     * 阿里云OSS
     */
    ALI_YUN(1, "阿里云OSS");

    private final int value;

    private final String reasonPhrase;


    OssTypeEnum(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public int value() {
        return this.value;
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }

    @Override
    public String toString() {
        return Integer.toString(this.value);
    }

    public static OssTypeEnum valueOf(int value) {
        for (OssTypeEnum from : values()) {
            if (from.value == value) {
                return from;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
