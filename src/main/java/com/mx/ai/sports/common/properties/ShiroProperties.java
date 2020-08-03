package com.mx.ai.sports.common.properties;

import lombok.Data;

/**
 * 权限相关配置
 * @author Mengjiaxin
 * @date 2019-08-20 16:24
 */
@Data
public class ShiroProperties {

    private long sessionTimeout;
    private int cookieTimeout;
    private String anonUrl;
    private String loginUrl;
    private String successUrl;
    private String logoutUrl;
    private String unauthorizedUrl;
}
