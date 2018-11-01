package com.example.demo.browser;

import com.example.demo.browser.authentication.ImoocAuthenticationFailureHandler;
import com.example.demo.browser.authentication.ImoocAuthenticationSuccessHandler;
import com.example.securitycore.propertites.SecurityProperties;
import com.example.securitycore.validate.code.ValidateCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {



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
        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
        validateCodeFilter.setFailureHandler(imoocAuthenticationFailureHandler);
        validateCodeFilter.setSecurityProperties(securityProperties);
        validateCodeFilter.afterPropertiesSet();
        http
                .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()
//                .loginPage("/imooc-signIn.html")
                .loginPage("/authentication/require")
                .loginProcessingUrl("/authentication/form")
                .successHandler(imoocAuthenticationSuccessHandler)
                .failureHandler(imoocAuthenticationFailureHandler)
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
                .antMatchers("/authentication/require",
                        securityProperties.getBrowser().getLoginPage(),
                        "/code/*"
                ).permitAll()
                .anyRequest()
                .authenticated()
                .and().csrf().disable();
    }
}
