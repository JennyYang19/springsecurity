package com.example.demo.service.impl;

import com.example.demo.service.HelloService;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018/10/3.
 */
@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public String greeting(String name) {
        System.out.println("greet");
        return "hello "+name;
    }
}
