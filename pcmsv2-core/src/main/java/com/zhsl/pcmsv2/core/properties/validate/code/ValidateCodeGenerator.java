package com.zhsl.pcmsv2.core.properties.validate.code;


import org.springframework.web.context.request.ServletWebRequest;

public interface ValidateCodeGenerator {

    ValidateCode generate(ServletWebRequest request);

}
