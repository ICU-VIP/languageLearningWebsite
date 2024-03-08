package org.cdtu.website.mapper;

import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.cdtu.website.entity.Review;

@Mapper
public interface ReviewMapper extends BaseMapper<Review> {
}
