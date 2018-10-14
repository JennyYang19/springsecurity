package com.example.demo.controller;

import com.example.demo.exception.UserNotExistExceptioin;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(UserNotExistExceptioin.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleUerNotFoundException(UserNotExistExceptioin existExceptioin) {
        System.out.println("-----------进入advice");
        Map<String, Object> result = new HashMap<>();
        result.put("id", existExceptioin.getId());
        result.put("message", existExceptioin.getMessage());
        return result;
    }


    @ResponseBody
    public Map<String, Object> handleSuccess(Object object) {
        System.out.println("-----------进入advice");
        Map<String, Object> result = new HashMap<>();
        result.put("object", object);
        return result;
    }
}

