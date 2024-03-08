package org.cdtu.website.service;

import com.mybatisflex.core.service.IService;
import org.cdtu.website.entity.ProblemSet;

public interface ProblemSetService extends IService<ProblemSet> {
    Boolean saveProblemSetAndAtomic(ProblemSet problemSet);

    Boolean updateProblemSetAndAtomic(ProblemSet problemSet);
}
