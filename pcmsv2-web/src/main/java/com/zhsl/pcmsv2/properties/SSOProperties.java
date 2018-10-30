package com.zhsl.pcmsv2.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "zhsl.pcmsv2")
public class SSOProperties {

    private String sSOLoginUri = "loginOther";

    private String sSOAuthenticationUrl = "http://114.215.150.204:20350/me";


    public String getsSOLoginUri() {
        return sSOLoginUri;
    }

    public void setsSOLoginUri(String sSOLoginUri) {
        this.sSOLoginUri = sSOLoginUri;
    }

    public String getsSOAuthenticationUrl() {
        return sSOAuthenticationUrl;
    }

    public void setsSOAuthenticationUrl(String sSOAuthenticationUrl) {
        this.sSOAuthenticationUrl = sSOAuthenticationUrl;
    }
}
