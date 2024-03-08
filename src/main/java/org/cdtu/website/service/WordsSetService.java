package org.cdtu.website.service;

import com.mybatisflex.core.service.IService;
import org.cdtu.website.entity.WordsSet;

import java.util.List;

public interface WordsSetService extends IService<WordsSet> {
    List<Integer> getWordIdListRandom(Integer wordsSetId, Integer count);
}
