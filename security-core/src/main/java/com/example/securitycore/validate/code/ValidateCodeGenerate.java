package com.example.securitycore.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

public interface ValidateCodeGenerate {

    ValidateCode generate(ServletWebRequest request);
}
