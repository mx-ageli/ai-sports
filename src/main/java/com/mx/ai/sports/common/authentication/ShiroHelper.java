package com.mx.ai.sports.common.authentication;

import com.mx.ai.sports.common.annotation.Helper;
import org.apache.shiro.authz.AuthorizationInfo;

/**
 * Shiro 帮助类
 * @author Mengjiaxin
 * @date 2019-08-20 16:11
 */
@Helper
public class ShiroHelper extends ShiroRealm {

    /**
     * 获取当前用户的角色和权限集合
     *
     * @return AuthorizationInfo
     */
    public AuthorizationInfo getCurrentuserAuthorizationInfo() {
        return super.doGetAuthorizationInfo(null);
    }
}
