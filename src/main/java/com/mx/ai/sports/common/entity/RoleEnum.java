package com.mx.ai.sports.common.entity;

/**
 * 默认的两种用户角色
 * @author Mengjiaxin
 * @date 2020/8/5 4:06 下午
 */
public enum RoleEnum {
    /**
     * 1学生
     */
    STUDENT(1L, "学生"),

    /**
     * 2老师
     */
    TEACHER(2L, "老师");

    private final Long value;

    private final String reasonPhrase;


    RoleEnum(Long value, String reasonPhrase) {
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
