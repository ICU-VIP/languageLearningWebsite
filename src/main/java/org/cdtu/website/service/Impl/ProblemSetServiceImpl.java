package org.cdtu.website.service.Impl;

import com.mybatisflex.core.util.SqlUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.cdtu.website.entity.AtomicProblem;
import org.cdtu.website.entity.ProblemSet;
import org.cdtu.website.mapper.AtomicProblemMapper;
import org.cdtu.website.mapper.ProblemSetMapper;
import org.cdtu.website.mapper.UserMapper;
import org.cdtu.website.service.ProblemSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProblemSetServiceImpl extends ServiceImpl<ProblemSetMapper, ProblemSet> implements ProblemSetService {
    ProblemSetMapper problemSetMapper;

    @Autowired
    public void setProblemSetMapper(ProblemSetMapper problemSetMapper) {
        this.problemSetMapper = problemSetMapper;
    }

    AtomicProblemMapper atomicProblemMapper;

    @Autowired
    public void setAtomicProblemMapper(AtomicProblemMapper atomicProblemMapper) {
        this.atomicProblemMapper = atomicProblemMapper;
    }

    @Override
    @Transactional
    public Boolean saveProblemSetAndAtomic(ProblemSet problemSet) {
        boolean result;
        result = SqlUtil.toBool(problemSetMapper.insert(problemSet, true));
        for (AtomicProblem atomicProblem : problemSet.getAtomicProblems()) {
            atomicProblem.setSetId(problemSet.getId());
        }
        if (!problemSet.getAtomicProblems().isEmpty())
            result &= SqlUtil.toBool(atomicProblemMapper.insertBatch(problemSet.getAtomicProblems()));
        return result;
    }

    @Override
    @Transactional
    public Boolean updateProblemSetAndAtomic(ProblemSet problemSet) {
        boolean result;
        result = SqlUtil.toBool(problemSetMapper.update(problemSet));
        for (AtomicProblem atomicProblem : problemSet.getAtomicProblems()) {
            atomicProblem.setSetId(problemSet.getId());
            result &= SqlUtil.toBool(atomicProblemMapper.insertOrUpdate(atomicProblem));
        }
        return result;
    }
}
