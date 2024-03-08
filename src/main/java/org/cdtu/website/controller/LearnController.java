package org.cdtu.website.controller;

import com.mybatisflex.core.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.cdtu.website.common.BitMapUtil;
import org.cdtu.website.entity.*;
import org.cdtu.website.entity.table.ReviewTableDef;
import org.cdtu.website.entity.table.WordsTableDef;
import org.cdtu.website.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@Slf4j
@RequestMapping("/learn")
@CrossOrigin
public class LearnController {

    WordsSetService wordsSetService;

    @Autowired
    public void setWordsSetService(WordsSetService wordsSetService) {
        this.wordsSetService = wordsSetService;
    }

    LearningRecordsService learningRecordsService;

    @Autowired
    public void setLearningRecordsService(LearningRecordsService learningRecordsService) {
        this.learningRecordsService = learningRecordsService;
    }

    UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    WordsService wordsService;


    @Autowired
    public void setWordsService(WordsService wordsService) {
        this.wordsService = wordsService;
    }

    ReviewService reviewService;

    @Autowired
    public void setReviewService(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    ExperienceService experienceService;

    @Autowired
    public void setExperienceService(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }



    @Value("${review.word.count}")
    private Integer MAXREVIEWCOUNT;

    @GetMapping("/wordsSet")
    public HttpResult<List<WordsSet>> getWordsSet() {
        List<WordsSet> wordsSetList = wordsSetService.list();
//        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        QueryWrapper queryWrapper = QueryWrapper.create()
//                .select(UserTableDef.USER.ID)
//                .where(UserTableDef.USER.EMAIL.eq(email));
        Long id = userService.getCurrentUserId();
        LearningRecords learningRecords = learningRecordsService.getById(id);
        if (!Objects.isNull(learningRecords)) {
            for (WordsSet wordsSet : wordsSetList) {
                wordsSet.setTotal(BitMapUtil.getBitCount(wordsSet.getWordsBitmap()));
                //在此集合中的背诵了的单词
                Byte[] curLearnedBitmap = BitMapUtil.getANDBitMap(learningRecords.getWordsBitmap(), wordsSet.getWordsBitmap());
                int wordCount = BitMapUtil.getBitCount(curLearnedBitmap);
                wordsSet.setLearned(wordCount);
                wordsSet.setPercentage(Math.floorDiv(wordCount * 100, wordsSet.getTotal()));
            }
        } else {
            for (WordsSet wordsSet : wordsSetList) {
                wordsSet.setTotal(BitMapUtil.getBitCount(wordsSet.getWordsBitmap()));
                wordsSet.setLearned(0);
                wordsSet.setPercentage(0);
            }
        }
        return HttpResult.success(wordsSetList);
        // 返回一个包含单词集列表的HttpResult对象
    }

    @GetMapping("/learningRecords")
    public HttpResult<LearningRecords> getLearningRecords() {
        //SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        QueryWrapper queryWrapper = QueryWrapper.create()
//                .select(UserTableDef.USER.ID)
//                .where(UserTableDef.USER.EMAIL.eq(email));
        Long id = userService.getCurrentUserId();
        LearningRecords learningRecords = learningRecordsService.getById(id);
        // 可能为空 暂未处理
        learningRecords.setWordCount(BitMapUtil.getBitCount(learningRecords.getWordsBitmap()));
        return HttpResult.success(learningRecords);
    }

    @GetMapping("/getWords")
    public HttpResult<Object> getWordsByWordsSetId(@RequestParam Integer wordsSetId, @RequestParam Integer count) {
        List<Integer> idList = wordsSetService.getWordIdListRandom(wordsSetId, count);
        User user = userService.getCurrentUser();
        Integer reviewCnt = user.getReviewCnt();
        QueryWrapper queryWrapper2 = QueryWrapper.create()
                .select()
                .where(ReviewTableDef.REVIEW.USER_ID.eq(user.getId()))
                .limit(reviewCnt);
        List<Review> reviewList = reviewService.list(queryWrapper2);
        for (Review review : reviewList) {
            idList.add(review.getWordId());
        }
        if (!idList.isEmpty()) {
            List<Words> wordsList = wordsService.listByIds(idList);
            return HttpResult.success(wordsList);
        } else {
            return HttpResult.builder()
                    .msg("全部单词已经完成")
                    .code(HttpResult.SUCCESS_CODE)
                    .build();
        }
    }

    @PutMapping("/updateLearningRecords/{id}")
    @Transactional
    public HttpResult<Object> saveLearningRecords(@PathVariable("id") Integer wordId) {
        learningRecordsService.saveLearningRecords(wordId);
        User user = userService.getCurrentUser();
        Long id = user.getId();
        QueryWrapper queryWrapper1 = QueryWrapper.create()
                .select()
                .where(ReviewTableDef.REVIEW.USER_ID.eq(id))
                .and(ReviewTableDef.REVIEW.WORD_ID.eq(wordId));
        Review review = reviewService.getOne(queryWrapper1);
        //查看单词有多少经验值
        QueryWrapper queryWrapper2 = QueryWrapper.create()
                .select(WordsTableDef.WORDS.LEVEL)
                .where(WordsTableDef.WORDS.ID.eq(wordId));
        Integer exp = wordsService.getOneAs(queryWrapper2, Integer.class);

        if (Objects.isNull(review)) {
            review = new Review();
            review.setUserId(id);
            review.setWordId(wordId);
            review.setCount(1);
            reviewService.save(review);
            //更新经验值
            // 1.Table user exp
            // 2.Table experience exp

            Experience experience = Experience.builder()
                    .userId(id)
                    .exp(exp)
                    .type(Experience.NEW_WORD)
                    .build();
            experienceService.save(experience);
        } else {
            int reviewCnt = review.getCount();
            if (reviewCnt > MAXREVIEWCOUNT) {
                reviewService.removeById(review);
            } else {
                review.setCount(reviewCnt + 1);
                reviewService.updateById(review);
            }
            //更新经验值
            Experience experience = Experience.builder()
                    .userId(id)
                    .exp(exp)
                    .type(Experience.REVIEW_WORD)
                    .build();
            experienceService.save(experience);
        }
        user.setExp(user.getExp() + exp);
        userService.updateById(user);
        // reviewService.saveOrUpdate();
        return HttpResult.success(null);
    }

    @GetMapping("/resetLearningRecords/{id}")
    public HttpResult<Object> resetLearningRecords(@PathVariable("id") Integer wordsSetId) {
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        QueryWrapper queryWrapper = QueryWrapper.create()
//                .select(UserTableDef.USER.ID)
//                .where(UserTableDef.USER.EMAIL.eq(email));
        Long id = userService.getCurrentUserId();
        LearningRecords learningRecords = learningRecordsService.getById(id);
        WordsSet wordsSet = wordsSetService.getById(wordsSetId);
        learningRecords.setWordsBitmap(BitMapUtil.getXORBitMap(learningRecords.getWordsBitmap(), wordsSet.getWordsBitmap()));
        learningRecords.setWordCount(BitMapUtil.getBitCount(learningRecords.getWordsBitmap()));
        learningRecordsService.updateById(learningRecords);
        return HttpResult.success(learningRecords);
    }




}

