package org.cdtu.website.service.Impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.cdtu.website.common.BitMapUtil;
import org.cdtu.website.entity.LearningRecords;
import org.cdtu.website.entity.table.UserTableDef;
import org.cdtu.website.mapper.LearningRecordsMapper;
import org.cdtu.website.mapper.UserMapper;
import org.cdtu.website.service.LearningRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LearningRecordsServiceImpl extends ServiceImpl<LearningRecordsMapper, LearningRecords> implements LearningRecordsService {
    LearningRecordsMapper learningRecordsMapper;

    @Autowired
    public void setLearningRecordsMapper(LearningRecordsMapper learningRecordsMapper) {
        this.learningRecordsMapper = learningRecordsMapper;
    }

    UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public void saveLearningRecords(Integer wordId) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(UserTableDef.USER.ID)
                .where(UserTableDef.USER.EMAIL.eq(email));
        Long userId = userMapper.selectOneByQueryAs(queryWrapper, Long.class);
        LearningRecords learningRecords = learningRecordsMapper.selectOneById(userId);
        if (Objects.isNull(learningRecords)) {
            LearningRecords newLearningRecords = new LearningRecords();
            newLearningRecords.setUserId(userId);
            Byte[] bitMap = new Byte[BitMapUtil.BITMAP_SIZE];
//            int byteIndex = wordId / 8;
//            int bitIndex = wordId % 8;
//            bitMap[byteIndex] |= (byte) (1 << bitIndex);
            BitMapUtil.SetByOffset(bitMap, wordId);
            newLearningRecords.setWordsBitmap(bitMap);
            learningRecordsMapper.insert(newLearningRecords);
        } else {
            Byte[] bitMap = learningRecords.getWordsBitmap();
//            int byteIndex = wordId / 8;
//            int bitIndex = wordId % 8;
//            bitMap[byteIndex] |= (byte) (1 << bitIndex);
            BitMapUtil.SetByOffset(bitMap, wordId);
            learningRecords.setWordsBitmap(bitMap);
            learningRecordsMapper.update(learningRecords);
        }
    }
}