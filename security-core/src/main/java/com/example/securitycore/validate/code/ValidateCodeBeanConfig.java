package com.example.securitycore.validate.code;

import com.example.securitycore.propertites.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidateCodeBeanConfig {

    @Autowired
    private SecurityProperties securityProperties;


    @Bean   //和在ImageCodeGenerate@component效果一样
    @ConditionalOnMissingBean(name = "imageCodeGenerate")  //如果容器里有一个imageCodeGenerate 就不用这个bean,没有就用这
    public ValidateCodeGenerate imageCodeGenerate() {
        ImageCodeGenerate codeGenerate=new ImageCodeGenerate();
        codeGenerate.setSecurityProperties(securityProperties);
        return codeGenerate;
    }
}
