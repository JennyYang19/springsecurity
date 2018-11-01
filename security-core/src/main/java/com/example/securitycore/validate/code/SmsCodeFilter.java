package com.example.securitycore.validate.code;

import com.example.securitycore.propertites.SecurityProperties;
import com.example.securitycore.validate.code.image.ImageCode;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
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
import java.util.HashSet;
import java.util.Set;

@Data
public class SmsCodeFilter extends OncePerRequestFilter implements InitializingBean { //保证过滤器每次只会被调用一次

    private AuthenticationFailureHandler failureHandler;

    private SessionStrategy sessionStrategy=new HttpSessionSessionStrategy();


    private Set<String> urls = new HashSet<>();

    private SecurityProperties securityProperties;

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        String[] configUrls=StringUtils.splitByWholeSeparatorPreserveAllTokens(securityProperties.getCode().getSms().getUrl(),",");
        if (configUrls!=null) {
            for (String url : configUrls) {
                urls.add(url);
            }
        }

        urls.add("/authentication/mobile");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        boolean action=false;//在yaml文件里面配置哪些url需要校验验证码
        for (String s :
                urls) {
            if (pathMatcher.match(s, httpServletRequest.getRequestURI())) {
                action=true;
            }
        }

        if (action) {
            try {
                validate(new ServletWebRequest(httpServletRequest));
            } catch (ValidateCodeException e) {

                failureHandler.onAuthenticationFailure(httpServletRequest,httpServletResponse,e);
                return;//失败了直接返回
            }
        }
            filterChain.doFilter(httpServletRequest,httpServletResponse);

    }



    public void validate(ServletWebRequest request) {


       ValidateCode codeInSession = (ValidateCode) sessionStrategy.getAttribute(request, ValidateCodeProcessor.SESSION_KEY_PREFIX+"SMS");

        String codeInRequest;
        try {
            codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),
                   "smsCode");
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
            sessionStrategy.removeAttribute(request, ValidateCodeProcessor.SESSION_KEY_PREFIX+"SMS");//过期了把session移除
//            throw new ValidateCodeException(processorType + "验证码已过期");
            throw new ValidateCodeException( "验证码已过期");
        }

        if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
//            throw new ValidateCodeException(processorType + "验证码不匹配");
            throw new ValidateCodeException( "验证码不匹配");
        }

        sessionStrategy.removeAttribute(request,ValidateCodeProcessor.SESSION_KEY_PREFIX+"SMS");
    }

}
