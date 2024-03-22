package org.cdtu.website.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table(value = "posts")
public class Posts {
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Long id;
    private String title;
    private String content;
    private Long createUser;
    @Column(onInsertValue = "now()") // 插入时设置默认值为当前时间
    private LocalDateTime createTime;
    private Long updateUser;
    @Column(onUpdateValue = "now()")
    private LocalDateTime updateTime;
}
