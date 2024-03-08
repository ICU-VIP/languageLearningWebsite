package org.cdtu.website.service.Impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.cdtu.website.entity.Posts;
import org.cdtu.website.mapper.PostsMapper;
import org.cdtu.website.service.PostsService;
import org.springframework.stereotype.Service;

@Service
public class PostsServiceImpl extends ServiceImpl<PostsMapper, Posts> implements PostsService {
}
