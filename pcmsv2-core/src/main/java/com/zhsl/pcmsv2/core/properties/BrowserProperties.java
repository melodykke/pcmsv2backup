package com.zhsl.pcmsv2.core.properties;

public class BrowserProperties {
    private String loginPage = "/imooc-sign-in.html";

    private LoginType loginType = LoginType.JSON;

    private int rememberMeSeconds = 3600;


    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }

    public int getRememberMeSeconds() {
        return rememberMeSeconds;
    }

    public void setRememberMeSeconds(int rememberMeSeconds) {
        this.rememberMeSeconds = rememberMeSeconds;
    }
}
