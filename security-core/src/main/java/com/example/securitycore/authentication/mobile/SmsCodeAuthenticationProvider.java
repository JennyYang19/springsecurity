package com.example.securitycore.authentication.mobile;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;


@Data
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserDetailsService userDetailsService;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        SmsCodeAuthenticationToken authenticationToken=(SmsCodeAuthenticationToken) authentication;
        UserDetails userDetails = userDetailsService.loadUserByUsername((String) authenticationToken.getPrincipal());
        if (userDetails == null) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }

        SmsCodeAuthenticationToken codeAuthenticationToken = new SmsCodeAuthenticationToken(userDetails, userDetails.getAuthorities());

        codeAuthenticationToken.setDetails(authenticationToken.getDetails());

        return codeAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {

        //判断传进来的是不是smsCodeAuthenticationToken这种类型
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
