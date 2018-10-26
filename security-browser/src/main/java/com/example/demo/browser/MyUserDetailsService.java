package com.example.demo.browser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;



@Component
public class MyUserDetailsService implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        //根据用户名查用户信息
        logger.info("登录用户名："+s);

        //根据查找到的用户信息判断是否被冻结
        return new User(s,passwordEncoder.encode("123"),true,true,true,true,AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));


//        return new User(s,"123", AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }
}
