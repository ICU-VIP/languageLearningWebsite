package org.cdtu.website.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Table(value = "experience")
@Builder
public class Experience {
    public static final Short NEW_WORD = 0;
    public static final Short REVIEW_WORD = 1;
    public static final Short ANSWER_QUESTION = 3;
    @Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
    private Long id;
    private Long userId;
    private Integer exp;
    private Short type;
    @Column(onInsertValue = "now()")
    private LocalDateTime createTime;
    // 省略gette
}
