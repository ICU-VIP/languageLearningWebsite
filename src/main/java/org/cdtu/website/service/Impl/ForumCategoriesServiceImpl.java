package org.cdtu.website.service.Impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.cdtu.website.entity.ForumCategories;
import org.cdtu.website.mapper.ForumCategoriesMapper;
import org.cdtu.website.service.ForumCategoriesService;
import org.springframework.stereotype.Service;

@Service
public class ForumCategoriesServiceImpl
        extends ServiceImpl<ForumCategoriesMapper, ForumCategories>
        implements ForumCategoriesService {
}
