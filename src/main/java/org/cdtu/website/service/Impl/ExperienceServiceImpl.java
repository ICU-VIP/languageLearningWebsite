package org.cdtu.website.service.Impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.cdtu.website.entity.Experience;
import org.cdtu.website.mapper.ExperienceMapper;
import org.cdtu.website.service.ExperienceService;
import org.springframework.stereotype.Service;

@Service
public class ExperienceServiceImpl extends ServiceImpl<ExperienceMapper, Experience> implements ExperienceService {
}
