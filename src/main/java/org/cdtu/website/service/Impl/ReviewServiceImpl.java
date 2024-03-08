package org.cdtu.website.service.Impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.cdtu.website.entity.Review;
import org.cdtu.website.mapper.ReviewMapper;
import org.cdtu.website.service.ReviewService;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review> implements ReviewService {
}
