package org.cdtu.website.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

@Data
@Table(value = "memory_words")
public class Words {
    @Id(keyType = KeyType.Auto)
    private Integer id;
    private String word;
    private String sw;
    private String phonetic;
    private String definition;
    private String translation;
    private String exchange;
    private String pos;
    private Integer bnc;
    private Integer frq;
    private Integer dx; //自定义词频
    private Short level;
    private Long type;
}
