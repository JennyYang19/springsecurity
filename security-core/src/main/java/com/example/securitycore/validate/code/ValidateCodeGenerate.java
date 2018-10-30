package com.example.securitycore.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

public interface ValidateCodeGenerate {

    ImageCode generate(ServletWebRequest request);
}
