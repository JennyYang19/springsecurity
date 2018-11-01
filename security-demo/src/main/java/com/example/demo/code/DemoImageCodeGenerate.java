package com.example.demo.code;

import com.example.securitycore.validate.code.image.ImageCode;
import com.example.securitycore.validate.code.ValidateCodeGenerate;
import org.springframework.web.context.request.ServletWebRequest;

//@Component("imageCodeGenerate")   //覆盖默认的逻辑 以增量的方式去适应变化
public class DemoImageCodeGenerate implements ValidateCodeGenerate {
    @Override
    public ImageCode generate(ServletWebRequest request) {
        System.out.println("更高级的图形验证码生成代码");
        return null;
    }
}
