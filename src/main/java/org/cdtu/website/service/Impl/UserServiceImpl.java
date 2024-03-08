package org.cdtu.website.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.cdtu.website.common.JwtUtil;
import org.cdtu.website.common.ThreadLocalUtil;
import org.cdtu.website.entity.HttpResult;
import org.cdtu.website.entity.LoginUserDetails;
import org.cdtu.website.entity.User;
import org.cdtu.website.entity.table.UserTableDef;
import org.cdtu.website.mapper.UserMapper;
import org.cdtu.website.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    private AuthenticationManager authenticationManager;

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


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

    UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public HttpResult<Object> login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        user.getPassword()
                );

        String jwtToken = null;
        try {
            Authentication authentication =
                    authenticationManager.authenticate(authenticationToken);
            HashMap<String, Object> map = getTokenMap(user, authentication);
            jwtToken = JwtUtil.genToken(map);
            stringRedisTemplate.opsForValue().set("loginToken:" + jwtToken, objectMapper.writeValueAsString(user), 1, TimeUnit.HOURS);
//            LoginUserDetails
//                    loginUserDetails = (LoginUserDetails) authentication.getPrincipal();
//            ThreadLocalUtil.set(loginUserDetails.user().getId());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            return HttpResult.error(e.getMessage());
        }
        return HttpResult.success(jwtToken);
    }

    private HashMap<String, Object> getTokenMap(User user, Authentication authentication) throws JsonProcessingException {
//        if (Objects.isNull(authentication)) {
//            throw new RuntimeException("用户名或密码错误");
//        }
        //spring security 已经校验了
        LoginUserDetails loginUserDetails =
                (LoginUserDetails) authentication.getPrincipal();
        List<SimpleGrantedAuthority> authList = loginUserDetails.getAuthorities();
        List<String> authStrList = authList.stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .toList();

        // String jwtToken = jwtUtils.creatJwt(userInfo, authStrArray);
        HashMap<String, Object> map = new HashMap<>();
        map.put("userInfo", user.getEmail());
        map.put("authorities", objectMapper.writeValueAsString(authStrList));
        //map.put("userId", user.getId());
        return map;
    }

    @Override
    public Long getCurrentUserId() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(UserTableDef.USER.ID)
                .where(UserTableDef.USER.EMAIL.eq(email));
        return userMapper.selectOneByQueryAs(queryWrapper, Long.class);
    }

    @Override
    public User getCurrentUser() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select()
                .where(UserTableDef.USER.EMAIL.eq(email));
        return userMapper.selectOneByQuery(queryWrapper);
    }
}
