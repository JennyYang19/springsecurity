package com.example.securitycore.validate.code;


import org.springframework.security.core.AuthenticationException;

public class ValidateCodeException extends AuthenticationException {


    private static final long serialVersionUID = 5348178588215383437L;

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
