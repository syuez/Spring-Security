package com.syuez.springsecurity.common;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * <a href="https://xie.infoq.cn/article/a5614c477aa97be61ae2a0ee6">SpringSecurity 添加验证码</a>
 */
public class KaptchaAuthenticationProvider extends DaoAuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(requestAttributes != null) {
            HttpServletRequest req = requestAttributes.getRequest();
            String kaptcha = req.getParameter("captcha");
            String sessionKaptcha = (String) req.getSession().getAttribute("captcha");
            if (kaptcha != null && kaptcha.equalsIgnoreCase(sessionKaptcha)) {
                return super.authenticate(authentication);
            }
        }
        throw new AuthenticationServiceException("验证码输入错误");
    }
}

