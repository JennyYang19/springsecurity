package com.example.securitycore.authentication.mobile;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 封装登录信息
 *
 * 在身份认证之前封装的是传上来的手机号
 * 身份认证成功之后封装的是用户信息
 *
 */
public class SmsCodeAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 420L;
    private final Object principal; //认证之前放手机号，认证之后放用户信息
//    private Object credentials;  //

    public SmsCodeAuthenticationToken(String mobile) {
        super((Collection)null);
        this.principal = mobile;
//        this.credentials = credentials;
        this.setAuthenticated(false);
    }

    public SmsCodeAuthenticationToken(Object principal,  Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
//        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    public Object getCredentials() {
        return null;
    }

    public Object getPrincipal() {
        return this.principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        } else {
            super.setAuthenticated(false);
        }
    }

    public void eraseCredentials() {
        super.eraseCredentials();
//        this.credentials = null;
    }
}

