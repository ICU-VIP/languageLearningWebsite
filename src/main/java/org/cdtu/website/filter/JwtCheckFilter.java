package org.cdtu.website.filter;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.cdtu.website.common.JwtUtil;
import org.cdtu.website.common.ServletUtils;
import org.cdtu.website.common.ThreadLocalUtil;
import org.cdtu.website.entity.HttpResult;
import org.cdtu.website.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class JwtCheckFilter extends OncePerRequestFilter {

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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if (requestURI.equals("/user/login") ||
                requestURI.equals("/code/image") ||
                requestURI.equals("/") ||
                requestURI.equals("/user/register") ||
                requestURI.equals("/hello")
        ) {
            doFilter(request, response, filterChain);
            return;
        }

        String jwtToken = request.getHeader("Authorization");
        if (!StringUtils.hasText(jwtToken)) {
//            HttpResult<Object> httpResult = HttpResult.builder()
//                    .code(HttpResult.UNAUTHORIZED)
//                    .msg("未登录!")
//                    .build();
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            //jwtUtils.printToken(request, response, httpResult);
            //ServletUtils.renderString(response, objectMapper.writeValueAsString(httpResult));
            return;
        }

        // String jwtToken = jwtToken.replace("bearer ", "");
//        if(!StringUtils.hasLength(jwtToken)){
//            HttpResult<Object> httpResult = HttpResult.error("jwt为空！");
//
//            //jwtUtils.printToken(request, response, httpResult);
//            ServletUtils.renderString(response, objectMapper.writeValueAsString(httpResult));
//            return;
//        }

        Map<String, Object> map = null;
        try {
            map = JwtUtil.parseToken(jwtToken);
        } catch (JWTDecodeException e) {
            HttpResult<Object> httpResult = HttpResult.error("数据非法!");
            //jwtUtils.printToken(request, response, httpResult);
            ServletUtils.renderString(response, objectMapper.writeValueAsString(httpResult));
            return;
        }catch (TokenExpiredException e){
            HttpResult<Object> httpResult = HttpResult.error("登录已过期!");
            ServletUtils.renderString(response, objectMapper.writeValueAsString(httpResult));
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }


//jwtUtils.printToken(request, response, httpResult);

        String redisToken = stringRedisTemplate.opsForValue().get("loginToken:" + jwtToken);
        if (!StringUtils.hasLength(redisToken)) {
            HttpResult<Object> httpResult = HttpResult.error("您已退出，请重新登录！");
            //jwtUtils.printToken(request, response, httpResult);
            ServletUtils.renderString(response, objectMapper.writeValueAsString(httpResult));
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }
        String userInfo = (String) map.get("userInfo");
        //User user = objectMapper.readValue(userInfo, User.class);
//        String[] authArray = jwtUtils.getUserAuthFromToken();
//        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
//        for(String auth : authArray){
//            authorityList.add(new SimpleGrantedAuthority(auth));
//        }
        List<String> authoritiesStr = objectMapper.readValue((String) map.get("authorities"), new TypeReference<>() {});
        List<SimpleGrantedAuthority> authorities = authoritiesStr.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userInfo, null, authorities);
        //ThreadLocalUtil.set(map.get("userId"));
       // System.out.println(token);
        SecurityContextHolder.getContext().setAuthentication(token);
        doFilter(request, response, filterChain);
    }
}
