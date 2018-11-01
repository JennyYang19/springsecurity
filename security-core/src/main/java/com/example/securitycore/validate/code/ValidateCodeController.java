package com.example.securitycore.validate.code;


import com.example.securitycore.validate.code.image.ImageCode;
import com.example.securitycore.validate.code.sms.SmsCodeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
public class ValidateCodeController {

 /*   private SessionStrategy sessionStrategy=new HttpSessionSessionStrategy();

    public static final String SESSION_KEY = "SESSION_KEY_IMAGE_CODE";

    @Autowired
    private ValidateCodeGenerate imageCodeGenerate;

    @Autowired
    private ValidateCodeGenerate smsCodeGenerate;

    @Autowired
    private SmsCodeSender smsCodeSender;*/

    public static final String SESSION_KEY = "SESSION_KEY_IMAGE_CODE";

    @Autowired
    private Map<String,ValidateCodeProcessor> validateCodeProcessorMap;


    @GetMapping("/code/{type}")
    public void createCode(HttpServletRequest request, HttpServletResponse response, @PathVariable String type) throws Exception {
        ValidateCodeProcessor validateCodeProcessor = validateCodeProcessorMap.get(type + "CodeProcessor");
        validateCodeProcessor.create(new ServletWebRequest(request, response));
    }











//    /**
//     * 图形验证码
//     * @param request
//     * @param response
//     * @throws IOException
//     */
//    @GetMapping("/code/image")
//    public void createCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        ImageCode imageCode =(ImageCode)imageCodeGenerate.generate(new ServletWebRequest(request));
//        sessionStrategy.setAttribute(new ServletWebRequest(request),SESSION_KEY,imageCode);
//
//        ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
//    }
//
//
//    /**
//     * 短信验证码
//     * @param request
//     * @param response
//     * @throws IOException
//     */
//    @GetMapping("/code/sms")
//    public void createSms(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletRequestBindingException {
//        ValidateCode validateCode =smsCodeGenerate.generate(new ServletWebRequest(request));
//        sessionStrategy.setAttribute(new ServletWebRequest(request),SESSION_KEY,validateCode);
//        String mobile=ServletRequestUtils.getRequiredStringParameter(request,"mobile");
//        smsCodeSender.send(mobile,validateCode.getCode());
//
////        ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
//    }






}
