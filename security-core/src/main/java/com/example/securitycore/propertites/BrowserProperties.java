package com.example.securitycore.propertites;

import org.springframework.stereotype.Component;

public class BrowserProperties {

    private String loginPage = "/imooc-signIn.html"; //设置默认登录页
//    private String loginPage ; //设置默认登录页

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }
}
