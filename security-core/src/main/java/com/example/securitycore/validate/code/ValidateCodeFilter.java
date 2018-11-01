package com.example.securitycore.validate.code;

import com.example.securitycore.propertites.SecurityConstants;
import com.example.securitycore.propertites.SecurityProperties;
import com.example.securitycore.validate.code.image.ImageCode;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.example.securitycore.validate.code.ValidateCodeProcessor.SESSION_KEY_PREFIX;

@Data
@Component("validateCodeFilter")
public class ValidateCodeFilter  extends OncePerRequestFilter implements InitializingBean { //保证过滤器每次只会被调用一次

    private AuthenticationFailureHandler failureHandler;

    private SessionStrategy sessionStrategy=new HttpSessionSessionStrategy();

    /**
     * 存放所有需要检验校验码的url
     */
    private Map<String, ValidateCodeType> urlMap = new HashMap<>();

    /***
     * 系统中的校验码处理器
     */
    @Autowired
    private ValidateCodeProcessorHolder validateCodeProcessorHolder;

    /**
     * 验证请求url与配置url是否匹配的工具
     */

    private AntPathMatcher pathMatcher = new AntPathMatcher();


    private Set<String> urls = new HashSet<>();

    @Autowired
    private SecurityProperties securityProperties;





    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {


        ValidateCodeType type = getValidateCodeType(httpServletRequest);
        if (type != null) {
            logger.info("校验请求(" + httpServletRequest.getRequestURI() + ")中的验证码,验证码类型" + type);
            try {
                validateCodeProcessorHolder.findValidateCodeProcessor(type)
                        .validate(new ServletWebRequest(httpServletRequest, httpServletResponse));
                logger.info("验证码校验通过");
            } catch (ValidateCodeException exception) {
                failureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, exception);
                return;
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }

    /**
     * 获取校验码的类型，如果当前请求不需要校验，则返回null
     *
     * @param request
     * @return
     */
    private ValidateCodeType getValidateCodeType(HttpServletRequest request) {
        ValidateCodeType result = null;
        if (!StringUtils.equalsIgnoreCase(request.getMethod(), "get")) {
            Set<String> urls = urlMap.keySet();
            for (String url : urls) {
                if (pathMatcher.match(url, request.getRequestURI())) {
                    result = urlMap.get(url);
                }
            }
        }
        return result;
    }
    /**
     * 初始化要拦截的url配置信息
     */
    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();

        urlMap.put(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM, ValidateCodeType.IMAGE);
        addUrlToMap(securityProperties.getCode().getImage().getUrl(), ValidateCodeType.IMAGE);

        urlMap.put(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE, ValidateCodeType.SMS);
        addUrlToMap(securityProperties.getCode().getSms().getUrl(), ValidateCodeType.SMS);
    }

    /**
     * 将系统中配置的需要验证验证码的url根据验证的类型放入map
     *
     */
    protected void addUrlToMap(String urlString, ValidateCodeType type) {
        if (StringUtils.isNotBlank(urlString)) {
            String[] urls = StringUtils.splitByWholeSeparatorPreserveAllTokens(urlString, ",");
            for (String url : urls) {
                urlMap.put(url, type);
            }
        }
    }


    public void validate(ServletWebRequest request) {


       ImageCode codeInSession = (ImageCode) sessionStrategy.getAttribute(request, SESSION_KEY_PREFIX + "IMAGE");

        String codeInRequest;
        try {
            codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),
                   "imageCode");
        } catch (ServletRequestBindingException e) {
            throw new ValidateCodeException("获取验证码的值失败");
        }

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException( "验证码的值不能为空");
//            throw new ValidateCodeException(processorType + "验证码的值不能为空");
        }

        if (codeInSession == null) {
//            throw new ValidateCodeException(processorType + "验证码不存在");
            throw new ValidateCodeException("验证码不存在");
        }

        if (codeInSession.isExpired()) {
            sessionStrategy.removeAttribute(request, ValidateCodeController.SESSION_KEY);//过期了把session移除
//            throw new ValidateCodeException(processorType + "验证码已过期");
            throw new ValidateCodeException( "验证码已过期");
        }

        if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
//            throw new ValidateCodeException(processorType + "验证码不匹配");
            throw new ValidateCodeException( "验证码不匹配");
        }

        sessionStrategy.removeAttribute(request, ValidateCodeController.SESSION_KEY);
    }

}
