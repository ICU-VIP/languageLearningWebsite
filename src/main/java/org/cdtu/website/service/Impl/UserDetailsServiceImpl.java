package org.cdtu.website.service.Impl;

import com.mybatisflex.core.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.cdtu.website.entity.LoginUserDetails;
import org.cdtu.website.entity.User;
import org.cdtu.website.entity.table.UserTableDef;
import org.cdtu.website.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select()
                .where(UserTableDef.USER.EMAIL.eq(email));

        User user = userMapper.selectOneWithRelationsByQuery(queryWrapper);
        if(Objects.isNull(user)){
            // 用户不存在
            throw new UsernameNotFoundException("用户不存在");
        }

       return new LoginUserDetails(user);
    }



}
