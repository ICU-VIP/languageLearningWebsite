package org.cdtu.website.service;

import com.mybatisflex.core.service.IService;
import org.cdtu.website.entity.LearningRecords;

public interface LearningRecordsService extends IService<LearningRecords> {
    void saveLearningRecords(Integer wordId);
}
