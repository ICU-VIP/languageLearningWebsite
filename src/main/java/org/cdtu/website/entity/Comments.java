package org.cdtu.website.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table(value = "comments")
public class Comments {
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Long id;
    private String content;
    private Long postId;
    private Long createUser;
    private LocalDateTime createTime;
    private Long updateUser;
    private LocalDateTime updateTime;
}
