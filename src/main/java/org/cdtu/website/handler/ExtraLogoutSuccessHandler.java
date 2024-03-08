package org.cdtu.website.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.cdtu.website.common.ServletUtils;
import org.cdtu.website.entity.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
@Slf4j
public class ExtraLogoutSuccessHandler implements LogoutSuccessHandler {
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
       // log.info("logout哟");
        String jwtToken = request.getHeader("Authorization");

        //String jwtToken = auth.replace("bearer ", "");
       // String jwtToken = auth;
        if (!StringUtils.hasLength(jwtToken)) {
            HttpResult<Object> httpResult = HttpResult.error("jwt为空！");
            ServletUtils.renderString(response, objectMapper.writeValueAsString(httpResult));
            return;
        }


//        if (!jwtUtils.verifyToken(jwtToken)) {
//            HttpResult<Object> httpResult = HttpResult.error("jwt非法！");
//            jwtUtils.printToken(request, response, httpResult);
//            return;
//        }
        stringRedisTemplate.delete("loginToken:" + jwtToken);
        HttpResult<Object> httpResult = HttpResult.builder()
                .code(HttpResult.SUCCESS_CODE)
                .msg("退出成功！")
                .build();
        ServletUtils.renderString(response, objectMapper.writeValueAsString(httpResult));
        //jwtUtils.printToken(request, response, httpResult);
    }
}

