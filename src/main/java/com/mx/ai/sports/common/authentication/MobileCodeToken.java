package com.mx.ai.sports.common.authentication;

import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;

import java.io.Serializable;

/**
 * 手机验证码方式获得Token
 * @author Mengjiaxin
 * @date 2019-08-20 16:10
 */
public class MobileCodeToken implements HostAuthenticationToken, RememberMeAuthenticationToken, Serializable {

    private static final long serialVersionUID = 7389684940351729250L;

    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 记住我
     */
    private boolean rememberMe;
    /**
     * ip地址
     */
    private String host;

    /**
     * 重写getPrincipal方法
     */
    @Override
    public Object getPrincipal() {
        return mobile;
    }

    /**
     * 重写getCredentials方法
     */
    @Override
    public Object getCredentials() {
        return mobile;
    }

    public MobileCodeToken() { this.rememberMe = false; }

    public MobileCodeToken(String mobile) { this(mobile, false, null); }

    public MobileCodeToken(String mobile, boolean rememberMe) { this(mobile, rememberMe, null); }

    public MobileCodeToken(String mobile, boolean rememberMe, String host) {
        this.mobile = mobile;
        this.rememberMe = rememberMe;
        this.host = host;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public boolean isRememberMe() {
        return rememberMe;
    }
}
