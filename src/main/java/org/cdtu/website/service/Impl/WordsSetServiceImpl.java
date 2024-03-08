package org.cdtu.website.service.Impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.cdtu.website.common.BitMapUtil;
import org.cdtu.website.entity.LearningRecords;
import org.cdtu.website.entity.WordsSet;
import org.cdtu.website.entity.table.UserTableDef;
import org.cdtu.website.mapper.LearningRecordsMapper;
import org.cdtu.website.mapper.UserMapper;
import org.cdtu.website.mapper.WordsSetMapper;
import org.cdtu.website.service.UserService;
import org.cdtu.website.service.WordsSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WordsSetServiceImpl extends ServiceImpl<WordsSetMapper, WordsSet> implements WordsSetService {
    WordsSetMapper wordsSetMapper;

    @Autowired
    public void setWordsSetMapper(WordsSetMapper wordsSetMapper) {
        this.wordsSetMapper = wordsSetMapper;
    }

    UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    LearningRecordsMapper learningRecordsMapper;

    @Autowired
    public void setLearningRecordsMapper(LearningRecordsMapper learningRecordsMapper) {
        this.learningRecordsMapper = learningRecordsMapper;
    }

    UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<Integer> getWordIdListRandom(Integer wordsSetId, Integer count) {
        WordsSet wordsSet = wordsSetMapper.selectOneById(wordsSetId);
        Byte[] wordsBitmap = wordsSet.getWordsBitmap();
//        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        QueryWrapper queryWrapper = QueryWrapper.create()
//                .select(UserTableDef.USER.ID)
//                .where(UserTableDef.USER.EMAIL.eq(email));
//        Long userId = userMapper.selectOneByQueryAs(queryWrapper, Long.class);
        Long userId = userService.getCurrentUserId();
        LearningRecords learningRecords = learningRecordsMapper.selectOneById(userId);
        Byte[] learningBitmap = null;
        if (Objects.isNull(learningRecords)) {
            //没有学习记录就新建空的学习记录 // 暂时认为不可能发生
            learningBitmap = new Byte[BitMapUtil.BITMAP_SIZE];
            Arrays.fill(learningBitmap, (byte) 0);
            learningRecords = new LearningRecords();
            learningRecords.setUserId(userId);
            learningRecords.setWordsBitmap(learningBitmap);
            learningRecordsMapper.insert(learningRecords);
        } else {
            learningBitmap = learningRecords.getWordsBitmap();
        }

        // byte[] learningBitmap = new byte[wordsBitmap.length];
        Byte[] unLearningBitmap = new Byte[BitMapUtil.BITMAP_SIZE];
        for (int i = 0; i < wordsBitmap.length; i++) {
            unLearningBitmap[i] = (byte) (wordsBitmap[i] ^ learningBitmap[i]);
        }
        return getRandomIds(unLearningBitmap, count);
    }

    public static List<Integer> getRandomIds(Byte[] wordsBitmap, Integer count) {
        List<Integer> allIds = BitMapUtil.getAllOffsetPos(wordsBitmap);
        Set<Integer> randomIds = new HashSet<>();
        Random random = new Random();
        //从bitmap中把id取出来
//        for (int i = 0; i < wordsBitmap.length; i++) {
//            for (int k = 0; k < 8; k++) {
//                if (((wordsBitmap[i] >> k) & 1) == 1) {
//                    allIds.add((i << 3) + k);
//                }
//            }
//        }
        int n = allIds.size();
        if ((count << 2) < n) { // 总单词量不足(小于需要的单词数量的四倍)时，转为顺序添加
            while (randomIds.size() < count) {
                int randomIndex = random.nextInt(n);
                // 确保随机到的单词未被学习过
                randomIds.add(allIds.get(randomIndex));

            }
        } else {
            for (int i = 0; i < n && randomIds.size() < count; i++) {
                randomIds.add(allIds.get(i));
            }
        }

        return new ArrayList<>(randomIds);
    }
}

