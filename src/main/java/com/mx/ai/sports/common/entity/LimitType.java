package com.mx.ai.sports.common.entity;

/**
 * 限流枚举
 * @author Mengjiaxin
 * @date 2020/8/3 4:35 下午
 */
public enum LimitType {
    /**
     * 传统类型
     */
    CUSTOMER,
    /**
     *  根据 IP地址限制
     */
    IP
}
