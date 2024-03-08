package org.cdtu.website.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.cdtu.website.common.JwtUtil;
import org.cdtu.website.common.ServletUtils;
import org.cdtu.website.entity.HttpResult;
import org.cdtu.website.entity.LoginUserDetails;
import org.cdtu.website.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ExtraAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    StringRedisTemplate stringRedisTemplate;

    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        LoginUserDetails userDetails = (LoginUserDetails) authentication.getPrincipal();
        User user = userDetails.user();
        String userInfo = objectMapper.writeValueAsString(user);

       // List<SimpleGrantedAuthority> authList = (List<SimpleGrantedAuthority>) userDetails.getAuthorities();
        List<SimpleGrantedAuthority> authList =  userDetails.getAuthorities();


       // String jwtToken = jwtUtils.creatJwt(userInfo, authStrArray);
        HashMap<String, Object> map = new HashMap<>();
        map.put("userInfo", userInfo);
        map.put("authorities", authList);
        String jwtToken = JwtUtil.genToken(map);
        //request.setAttribute("Authorization", "bearer "+ jwtToken);
        HttpResult<String> httpResult = HttpResult.success(jwtToken);


        stringRedisTemplate.opsForValue().set("loginToken:"+jwtToken, objectMapper.writeValueAsString(authentication), 1, TimeUnit.HOURS);
        //jwtUtils.printToken(request, response, httpResult);
        ServletUtils.renderString(response, objectMapper.writeValueAsString(httpResult));
    }


}
