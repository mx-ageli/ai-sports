package com.mx.ai.sports.common.authentication;

import com.mx.ai.sports.system.entity.User;
import com.mx.ai.sports.system.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;

/**
 * 手机验证码方式认证
 *
 * @author Mengjiaxin
 * @date 2019-08-20 16:10
 */
@Slf4j
public class MobileCodeRealm extends AuthorizingRealm {

    @Resource
    private IUserService userService;

    /**
     * 认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        MobileCodeToken token;

        // 如果是PhoneToken，则强转，获取phone；否则不处理。
        if (authenticationToken instanceof MobileCodeToken) {
            token = (MobileCodeToken) authenticationToken;
        } else {
            return null;
        }

        String username = (String) token.getPrincipal();
        User user = userService.findByUsername(username);

        return new SimpleAuthenticationInfo(user, username, this.getName());
    }

    /**
     * 授权
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    @Override
    public boolean supports(AuthenticationToken var1) {
        return var1 instanceof MobileCodeToken;
    }
}
