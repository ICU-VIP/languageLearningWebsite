package org.cdtu.website.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

@Data
@Table("words_set")
public class WordsSet {
    @Id(keyType = KeyType.Auto)
    private Integer id;
    private String name;
    @JsonIgnore
    private Byte[] wordsBitmap;
    private Short offset;
    private Short order;
    @Column(ignore = true)
    private Integer percentage;
    @Column(ignore = true)
    private Integer learned;
    @Column(ignore = true)
    private Integer total;
}
