package org.cdtu.website.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "review")
public class Review {
    @Id(keyType = KeyType.None)
    private Long userId;
    @Id(keyType = KeyType.None)
    private Integer wordId;
    private Integer count;
    @Column(onInsertValue = "now()")
    private LocalDateTime createTime;
}
