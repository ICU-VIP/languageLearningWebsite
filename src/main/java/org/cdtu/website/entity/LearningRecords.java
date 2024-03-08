package org.cdtu.website.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table(value = "learning_records")
public class LearningRecords {
    @Id(keyType = KeyType.None)
    private Long userId;
    @JsonIgnore
    private Byte[] wordsBitmap;
    @Column(onUpdateValue = "now()")
    private LocalDateTime updateTime;
    @Column(onInsertValue = "now()")
    private LocalDateTime createTime;
    @Column(ignore = true)
    private Integer wordCount;
}
