package com.example.securitycore.validate.code.impl;

import com.example.securitycore.validate.code.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;

public abstract class  AbstractValidateCodeProcessor<C extends ValidateCode>   implements ValidateCodeProcessor {

    /**
     * 操作session的工具类
     */
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    //spring容器在启动时会查找所有这个ValidateCodeGenerate接口的实现，然后把bean的名字放到map里面去
    /**
     * 收集系统中所有ValidateCodeGenerate的实现
     *
     * 这叫spring的依赖搜索
     */
    @Autowired
    private Map<String, ValidateCodeGenerate> validateCodeGenerateMap;

    @Override
    public void create(ServletWebRequest request) throws Exception {
        C validateCode = generate(request);
        save(request, validateCode);
        send(request, validateCode);
    }

    private C generate(ServletWebRequest request) {
        String type = getProcessorType(request);
        ValidateCodeGenerate validateCodeGenerate = validateCodeGenerateMap.get(type + "CodeGenerator");
        return (C) validateCodeGenerate.generate(request);
    }

    /**
     * 保存校验码
     *
     * @param request
     * @param validateCode
     */
    private void save(ServletWebRequest request, C validateCode) {
        sessionStrategy.setAttribute(request, getSessionKey(request), validateCode);
    }

    /**
     * 构建验证码放入session时的key
     *
     * @param request
     * @return
     */
    private String getSessionKey(ServletWebRequest request) {
        return SESSION_KEY_PREFIX + getValidateCodeType(request).toString().toUpperCase();
    }

    /**
     * 发送校验码，由子类实现
     * @param request
     * @throws Exception
     */
    protected abstract void send(ServletWebRequest request, C validateCode) throws Exception;

    /**
     * 根据请求的后半段判断是sms还是image
     * @param request
     * @return
     */
    private String getProcessorType(ServletWebRequest request) {
        return StringUtils.substringAfter(request.getRequest().getRequestURI(), "/code/");
    }

    /**
     * 根据请求的url获取校验码的类型
     *
     * @param request
     * @return
     */
    private ValidateCodeType getValidateCodeType(ServletWebRequest request) {
        String type = StringUtils.substringBefore(getClass().getSimpleName(), "CodeProcessor");
        return ValidateCodeType.valueOf(type.toUpperCase());
    }


    @Override
    public void validate(ServletWebRequest request) {

        ValidateCodeType processorType = getValidateCodeType(request);
        String sessionKey = getSessionKey(request);

        C codeInSession = (C) sessionStrategy.getAttribute(request, sessionKey);

        String codeInRequest;
        try {
            codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),
                    processorType.getParamNameOnValidate());
        } catch (ServletRequestBindingException e) {
            throw new ValidateCodeException("获取验证码的值失败");
        }

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException(processorType + "验证码的值不能为空");
        }

        if (codeInSession == null) {
            throw new ValidateCodeException(processorType + "验证码不存在");
        }

        if (codeInSession.isExpired()) {
            sessionStrategy.removeAttribute(request, sessionKey);
            throw new ValidateCodeException(processorType + "验证码已过期");
        }

        if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException(processorType + "验证码不匹配");
        }

        sessionStrategy.removeAttribute(request, sessionKey);
    }


}
