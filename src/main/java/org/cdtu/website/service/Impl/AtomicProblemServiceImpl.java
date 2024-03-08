package org.cdtu.website.service.Impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.cdtu.website.entity.AtomicProblem;
import org.cdtu.website.mapper.AtomicProblemMapper;
import org.cdtu.website.service.AtomicProblemService;
import org.springframework.stereotype.Service;

@Service
public class AtomicProblemServiceImpl extends ServiceImpl<AtomicProblemMapper, AtomicProblem> implements AtomicProblemService {
}
