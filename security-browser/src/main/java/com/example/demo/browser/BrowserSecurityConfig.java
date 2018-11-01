package com.example.demo.browser;

import com.example.demo.browser.authentication.ImoocAuthenticationFailureHandler;
import com.example.demo.browser.authentication.ImoocAuthenticationSuccessHandler;
import com.example.securitycore.authentication.AbstractChannelSecurityConfig;
import com.example.securitycore.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.example.securitycore.propertites.SecurityConstants;
import com.example.securitycore.propertites.SecurityProperties;
import com.example.securitycore.validate.code.ValidateCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
public class BrowserSecurityConfig  extends AbstractChannelSecurityConfig {



    @Autowired
    private ImoocAuthenticationSuccessHandler imoocAuthenticationSuccessHandler;

    @Autowired
    private ImoocAuthenticationFailureHandler imoocAuthenticationFailureHandler;
    /**
     * 密码加密
     * @return
     */
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;


    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
//        tokenRepository.setCreateTableOnStartup(true);  //自动生成表
        return tokenRepository;
    }

    @Autowired
    private SecurityProperties securityProperties;



    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
//        validateCodeFilter.setFailureHandler(imoocAuthenticationFailureHandler);
//        validateCodeFilter.setSecurityProperties(securityProperties);
//        validateCodeFilter.afterPropertiesSet();
//
//        SmsCodeFilter smsCodeFilter = new SmsCodeFilter();
//        smsCodeFilter.setFailureHandler(imoocAuthenticationFailureHandler);
//        smsCodeFilter.setSecurityProperties(securityProperties);
//        smsCodeFilter.afterPropertiesSet();

        applyPasswordAuthenticationConfig(http);

        http
            .apply(validateCodeSecurityConfig)
                .and()
            .apply(smsCodeAuthenticationSecurityConfig)
                .and()
            .rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                .userDetailsService(userDetailsService)

//        http.httpBasic()
                .and()
            .authorizeRequests()
//                .antMatchers("/imooc-signIn.html").permitAll()
//                .antMatchers("/authentication/require").permitAll()
                .antMatchers(
                        SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
                        SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,
                        securityProperties.getBrowser().getLoginPage(),
                        SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX+"/*"
                ).permitAll()
                .anyRequest()
                .authenticated()
                .and().
            csrf().disable()
                ;
    }
}
