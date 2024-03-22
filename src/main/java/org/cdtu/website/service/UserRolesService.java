package org.cdtu.website.service;

import com.mybatisflex.core.service.IService;
import org.cdtu.website.entity.UserRoles;

import java.util.List;

public interface UserRolesService extends IService<UserRoles> {
    void delAndUpdate(Long id, List<Long> roleIds);
}
