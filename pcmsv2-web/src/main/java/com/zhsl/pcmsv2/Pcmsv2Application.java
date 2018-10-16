package com.zhsl.pcmsv2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.MultipartConfigElement;

@EnableSwagger2
@SpringBootApplication
@RestController
@MapperScan(value = {"com.zhsl.pcmsv2.mapper"})
public class Pcmsv2Application {
    public static void main(String[] args) {
        SpringApplication.run(Pcmsv2Application.class, args);
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello spring security";
    }

    /**
     * 文件上传配置
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //单个文件最大
        factory.setMaxFileSize("1024000KB"); //KB,MB
        /// 设置总上传数据总大小
        factory.setMaxRequestSize("10240000KB");
        return factory.createMultipartConfig();
    }
}
