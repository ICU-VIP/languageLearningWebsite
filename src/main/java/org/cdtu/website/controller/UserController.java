package org.cdtu.website.controller;

import com.mybatisflex.core.query.QueryWrapper;
import org.cdtu.website.common.BitMapUtil;
import org.cdtu.website.entity.HttpResult;
import org.cdtu.website.entity.LearningRecords;
import org.cdtu.website.entity.User;
import org.cdtu.website.entity.dto.PasswordDto;
import org.cdtu.website.entity.table.UserTableDef;
import org.cdtu.website.service.LearningRecordsService;
import org.cdtu.website.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Objects;


@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }
    private LearningRecordsService learningRecordsService;

    @Autowired
    public void setLearningRecordsService(LearningRecordsService learningRecordsService) {
        this.learningRecordsService = learningRecordsService;
    }

    @PostMapping("/register")
    public HttpResult<Object> register(String username, String password) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select()
                .where(UserTableDef.USER.EMAIL.eq(username));
        //查询用户
        User u = userService.getOne(queryWrapper);

        if (u == null) {
            //没有占用
            //注册
            //userService.register(username, password);
            User user = new User();
            user.setEmail(username);
            user.setUsername(username.substring(0, username.indexOf("@")));
            user.setPassword(passwordEncoder.encode(password));
            userService.save(user);

            //创建学习记录
            LearningRecords learningRecords = new LearningRecords();
            Byte[] learningBitmap = new Byte[BitMapUtil.BITMAP_SIZE];
            Arrays.fill(learningBitmap, (byte) 0);
            learningRecords = new LearningRecords();
            learningRecords.setUserId(user.getId());
            learningRecords.setWordsBitmap(learningBitmap);
            learningRecordsService.save(learningRecords);

            return HttpResult.success(null);
        } else {
            //占用
            return HttpResult.error("邮箱已被占用");
        }


    }

    @PostMapping("/login")
    public HttpResult<Object> login(String username, String password) {
        User user = new User();
        user.setEmail(username);
        user.setPassword(password);
        return userService.login(user);
    }

//    @PostMapping("/login")
//    public HttpResult<Object> login(String email,  String password) {
//        QueryWrapper queryWrapper = QueryWrapper.create()
//                .select()
//                .where(UserTableDef.USER.EMAIL.eq(email));
//        //根据用户名查询用户
//        User loginUser = userService.getOne(queryWrapper);
//        //判断该用户是否存在
//        if (loginUser == null) {
//            return HttpResult.error("邮箱错误");
//        }
//
//        //判断密码是否正确  loginUser对象中的password是密文
//        if (Md5Util.getMD5String(password).equals(loginUser.getPassword())) {
//            //登录成功
//            Map<String, Object> claims = new HashMap<>();
//            claims.put("id", loginUser.getId());
//            claims.put("username", loginUser.getUsername());
//            String token = JwtUtil.genToken(claims);
//            //把token存储到redis中
//            //ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
//            //operations.set(token,token,1, TimeUnit.HOURS);
//            return HttpResult.success(token);
//        }
//        return HttpResult.error("密码错误");
//    }

    @GetMapping("/userInfo")
    public HttpResult<User> userInfo(/*@RequestHeader(name = "Authorization") String token*/) {
        //根据用户名查询用户
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select()
                .where(UserTableDef.USER.EMAIL.eq(email));
        User user = userService.getOne(queryWrapper);
        // User user = userService.findByUserName(username);
        //获得头像路径
        return HttpResult.success(user);
    }

    @PutMapping("/update")
    public HttpResult<Object> update(@RequestBody @Validated User user) {
        userService.updateById(user);
        return HttpResult.success(null);
    }

    @PatchMapping("/updateAvatar")
    public HttpResult<Object> updateAvatar(@RequestParam String avatarUrl) {
        //userService.updateAvatar(avatarUrl);
        //return HttpResult.success(null);
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select()
                .where(UserTableDef.USER.EMAIL.eq(email));
        User user = userService.getOne(queryWrapper);
        user.setAvatar(avatarUrl);
        userService.updateById(user);
        return HttpResult.success("修改成功");
    }

    @PostMapping("/updatePwd")
    public HttpResult<Object> updatePwd(@RequestBody PasswordDto passwords, @RequestHeader("Authorization") String token) {
        //1.校验参数
        String oldPwd = passwords.getOld_pwd();
        String newPwd = passwords.getNew_pwd();
        String rePwd = passwords.getRe_pwd();

        if (!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)) {
            return HttpResult.error("缺少必要的参数");
        }

        //原密码是否正确
        //调用userService根据用户名拿到原密码,再和old_pwd比对
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        //User loginUser = userService.findByUserName(username);
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select()
                .where(UserTableDef.USER.EMAIL.eq(email));
        User loginUser = userService.getOne(queryWrapper);
        //if (!loginUser.getPassword().equals(Md5Util.getMD5String(oldPwd))) {
        if (!passwordEncoder.matches(oldPwd, loginUser.getPassword())) {
            return HttpResult.error("原密码填写不正确");
        }

        //newPwd和rePwd是否一样
        if (!rePwd.equals(newPwd)) {
            return HttpResult.error("两次填写的新密码不一样");
        }

        //2.调用service完成密码更新
        //userService.updatePwd(newPwd);
        loginUser.setPassword(passwordEncoder.encode(newPwd));
        userService.updateById(loginUser);
        //删除redis中对应的token
//        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
//        operations.getOperations().
//                delete("loginToken:"+token);
        stringRedisTemplate.delete("loginToken:" + token);
        return HttpResult.success(null);
    }
//    @PostMapping("/logout")
//    public  HttpResult<Object> logout(@RequestParam String token) {
//        System.out.println(token);
//        System.out.println("*********************************zzz");
//        return HttpResult.builder()
//                .code(HttpResult.SUCCESS_CODE)
//                .msg("退出成功")
//                .build();
//    }
}
