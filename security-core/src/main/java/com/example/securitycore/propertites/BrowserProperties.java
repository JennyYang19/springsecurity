package com.example.securitycore.propertites;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class BrowserProperties {

    private String loginPage = "/imooc-signIn.html"; //设置默认登录页
//    private String loginPage ; //设置默认登录页


    private LoginType loginType = LoginType.JSON;

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }
}
