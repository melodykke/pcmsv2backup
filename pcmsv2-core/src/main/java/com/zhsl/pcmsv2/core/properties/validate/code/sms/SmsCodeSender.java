package com.zhsl.pcmsv2.core.properties.validate.code.sms;

public interface SmsCodeSender {
    void send(String mobile, String code);
}
