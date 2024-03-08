package org.cdtu.website;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.relation.RelationManager;
import com.mybatisflex.core.row.Db;
import org.apache.ibatis.cursor.Cursor;
import org.cdtu.website.common.BitMapUtil;
import org.cdtu.website.common.TempUtils;
import org.cdtu.website.entity.*;
import org.cdtu.website.entity.table.RolesTableDef;
import org.cdtu.website.entity.table.UserTableDef;
import org.cdtu.website.mapper.*;
import org.cdtu.website.service.LearningRecordsService;
import org.cdtu.website.service.UserService;
import org.cdtu.website.service.WordsSetService;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
class WebsiteApplicationTests {

    @Autowired
    TempUtils tempUtils;

    @Test
    void contextLoads() {
        tempUtils.updateAllWordsSet();
    }

    @Autowired
    WordsSetMapper wordsSetMapper;

    @Autowired
    WordsMapper wordsMapper;

    @Test
    void contextLoads2() {




    }

    private boolean isBitSet(int position, byte[] wordsBitmap) {
        int byteIndex = position / 8;
        int bitIndex = position % 8;
        return (wordsBitmap[byteIndex] & (1 << bitIndex)) != 0;
    }

    @Autowired
    UserMapper userMapper;

    @Autowired
    RolesMapper rolesMapper;

    @Autowired
    UserRolesMapper userRolesMapper;

    @Test
    void contextLoads3() {
//		User u = new User();
//		u.setUsername("张三");
//		u.setPassword("13254654");
//		u.setEmail("123@qq.com");
//		userMapper.insert(u);
        Roles r = new Roles();
        r.setName("学生2");
        r.setDescription("asf");
        rolesMapper.insert(r);
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select()
                .where(UserTableDef.USER.USERNAME.eq("张三"));
        User u2 = userMapper.selectOneByQuery(queryWrapper);
//		QueryWrapper queryWrapper2 = QueryWrapper.create()
//				.select()
//				.where(RolesTableDef.ROLES.NAME.eq("管理员"));
//		Roles r2 = rolesMapper.selectOneByQuery(queryWrapper2);
        UserRoles userRoles = new UserRoles(u2.getId(), r.getId());
        userRolesMapper.insert(userRoles);
    }

    @Test
    void contextLoads4() {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select()
                .where(UserTableDef.USER.USERNAME.eq("c1"));
        //User user = userMapper.selectOneWithRelationsByQuery(queryWrapper);
        User user = userMapper.selectOneWithRelationsByQuery(queryWrapper);
        System.out.println(user.getAvatar());
        System.out.println(user.getRoles());
    }

    @Test
    void contextLoads5() {
        User user = new User();
        user.setUsername("张三");
        System.out.println(HttpResult.error(user.toString()));
    }

    @Autowired
    UserService userService;

    @Test
    void contextLoads6() {
        User user = new User();
        user.setEmail("admin");
        user.setPassword("password");
        userService.save(user);
        List<User> list = userService.list();
        System.out.println(list);
    }

    @Autowired
    LearningRecordsService learningRecordsService;

    @Test
    void contextLoads7() {
        //User user = userMapper.selectOneWithRelationsByQuery(queryWrapper);
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(UserTableDef.USER.ID)
                .where(UserTableDef.USER.USERNAME.eq("c1"));
        Long id = userService.getOneAs(queryWrapper, Long.class);
        System.out.println(id);
        LearningRecords learningRecords = learningRecordsService.getById(id);
        System.out.println(learningRecords);
        // System.out.println(user.getRoles());
    }

    @Autowired
    WordsSetService wordsSetService;

    @Test
    void contextLoads8() {

        List<WordsSet> list = wordsSetService.list();
        for (WordsSet wordsSet : list) {
            System.out.println(wordsSet.getName() + "  数量：" + BitMapUtil.getBitCount(wordsSet.getWordsBitmap()));
        }
        LearningRecords learningRecords = learningRecordsService.getById(109947603219345408L);
        learningRecords.setWordsBitmap(list.get(0).getWordsBitmap());
        learningRecordsService.updateById(learningRecords);


    }

    @Test
    void contextLoads9() {
//        LearningRecords learningRecords = learningRecordsService.getById(116758229312827392L);
//        System.out.println(BitMapUtil.getBitCount(learningRecords.getWordsBitmap()));
        Byte[] bitmap = new Byte[BitMapUtil.BITMAP_SIZE];
        Arrays.fill(bitmap, (byte) 0);
        bitmap[1000] = -127;
        System.out.println(BitMapUtil.getBitCount(bitmap));
        System.out.println(Integer.bitCount(-127));
    }
}
