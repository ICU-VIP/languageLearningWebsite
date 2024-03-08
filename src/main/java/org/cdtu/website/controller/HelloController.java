package org.cdtu.website.controller;

import com.mybatisflex.core.query.QueryWrapper;
import org.cdtu.website.common.ThreadLocalUtil;
import org.cdtu.website.entity.HttpResult;
import org.cdtu.website.entity.LoginUserDetails;
import org.cdtu.website.entity.User;
import org.cdtu.website.entity.table.UserTableDef;
import org.cdtu.website.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Autowired
    UserService userService;

    @GetMapping("/hello")
    public HttpResult<Object> hello() {
        return
                HttpResult.success("Hello, World!" );
    }

    public HttpResult<Object> register(@RequestParam String username, @RequestParam String password) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select()
                .where(UserTableDef.USER.EMAIL.eq("admin"));
        System.out.println(username + " " + password);
        //查询用户
        User user = userService.getOneAs(queryWrapper, User.class);
        System.out.println(user);
        return HttpResult.success(userService.getOne(queryWrapper));
    }
}
