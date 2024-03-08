package org.cdtu.website.service.Impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.cdtu.website.entity.Words;
import org.cdtu.website.mapper.WordsMapper;
import org.cdtu.website.service.WordsService;
import org.springframework.stereotype.Service;

@Service
public class WordsServiceImpl extends ServiceImpl<WordsMapper, Words> implements WordsService {
}
