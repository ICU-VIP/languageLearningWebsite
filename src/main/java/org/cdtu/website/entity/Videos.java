package org.cdtu.website.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table(value = "videos")
public class Videos {
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Long id;
    private String name;
    private String url;
    private String imgUrl;
    private String description;
    private Long createUser;
    @Column(onInsertValue = "now()")
    private LocalDateTime createTime;
}
