package org.cdtu.website.mapper;

import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.cdtu.website.entity.Words;

@Mapper
public interface WordsMapper extends BaseMapper<Words> {
}
