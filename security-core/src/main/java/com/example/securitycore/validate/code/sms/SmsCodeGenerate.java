package com.example.securitycore.validate.code.sms;

import com.example.securitycore.propertites.SecurityProperties;
import com.example.securitycore.validate.code.ValidateCode;
import com.example.securitycore.validate.code.ValidateCodeGenerate;
import lombok.Data;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

@Data
//@Component("smsCodeGenerate")
@Component("smsCodeGenerator")
public class SmsCodeGenerate implements ValidateCodeGenerate {



    @Autowired
    private SecurityProperties securityProperties;


    @Override
    public ValidateCode generate(ServletWebRequest request) {
        String code = RandomStringUtils.randomNumeric(securityProperties.getCode().getSms().getLength());
        return new ValidateCode(code,securityProperties.getCode().getSms().getExpireIn());
    }


}
