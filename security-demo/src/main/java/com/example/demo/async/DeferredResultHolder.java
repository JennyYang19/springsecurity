package com.example.demo.async;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;

@Component
public class DeferredResultHolder {
    public Map<String, DeferredResult<String>> getMap() {
        return map;
    }


    public void setMap(Map<String, DeferredResult<String>> map) {
        this.map = map;
    }

    private Map<String, DeferredResult<String>> map =new HashMap<String, DeferredResult<String>>();
//    private Map<String, String> map =new HashMap<String,String>();

}
