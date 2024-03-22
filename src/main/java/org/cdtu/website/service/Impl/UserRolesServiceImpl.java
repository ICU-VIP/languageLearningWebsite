package org.cdtu.website.service.Impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.cdtu.website.entity.UserRoles;
import org.cdtu.website.entity.table.UserRolesTableDef;
import org.cdtu.website.mapper.UserRolesMapper;
import org.cdtu.website.service.UserRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserRolesServiceImpl extends ServiceImpl<UserRolesMapper, UserRoles> implements UserRolesService {

    UserRolesMapper userRolesMapper;

    @Autowired
    public void setUserRolesMapper(UserRolesMapper userRolesMapper) {
        this.userRolesMapper = userRolesMapper;
    }

    @Override
    @Transactional
    public void delAndUpdate(Long id, List<Long> roleIds) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(UserRolesTableDef.USER_ROLES.USER_ID.eq(id));
        userRolesMapper.deleteByQuery(queryWrapper);
        for (Long roleId : roleIds) {
            UserRoles userRoles = new UserRoles();
            userRoles.setUserId(id);
            userRoles.setRoleId(roleId);
            userRolesMapper.insert(userRoles);
        }
    }


}
