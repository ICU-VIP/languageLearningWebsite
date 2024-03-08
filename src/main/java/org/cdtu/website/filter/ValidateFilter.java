package org.cdtu.website.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class ValidateFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestUIL = request.getRequestURI();
        if(!requestUIL.equals("/login/code")){
            //log.info("不需要验证码的请求");
            doFilter(request, response, filterChain);
            return;
        }
        validateCode(request, response, filterChain);

    }

    private void validateCode(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String code = request.getParameter("code");
        String captchaCodeInSession = (String) request.getSession().getAttribute("CAPTCHA_CODE");
        request.getSession().removeAttribute("captcha_code_error");
       // log.info("myFilter is running");
        if(!StringUtils.hasText(code)){
            request.getSession().setAttribute("captcha_code_error", "请输入验证码");
            response.sendRedirect("/login/toLogin");
            return;
        }
        if(!StringUtils.hasLength(captchaCodeInSession) || !code.equalsIgnoreCase(captchaCodeInSession)){
            request.getSession().setAttribute("captcha_code_error", "验证码错误");
            response.sendRedirect("/login/toLogin");
            return;
        }
//        if(!code.equalsIgnoreCase(captchaCodeInSession)){
//            request.getSession().setAttribute("captcha_code_error", "验证码错误");
//            response.sendRedirect("/login/toLogin");
//            return;
//        }
        log.info("suss_to_filter");
        request.getSession().removeAttribute("CAPTCHA_CODE");
        this.doFilter(request, response, filterChain);
    }
}
