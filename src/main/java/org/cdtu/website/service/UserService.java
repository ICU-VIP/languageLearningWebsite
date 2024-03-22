package org.cdtu.website.service;


import com.mybatisflex.core.service.IService;
import org.cdtu.website.entity.HttpResult;
import org.cdtu.website.entity.User;

public interface UserService extends IService<User> {
    HttpResult<Object> login(User user);

    Long getCurrentUserId();

    User getCurrentUser();

    String getAvatar(Long id);
}
