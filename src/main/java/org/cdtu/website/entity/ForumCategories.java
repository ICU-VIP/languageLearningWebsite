package org.cdtu.website.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.Data;

@Data
@Table(value ="forum_categories")
public class ForumCategories {
    // 分类ID
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Integer id;
    private String name;
    private String description;
}