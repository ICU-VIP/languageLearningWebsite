package org.cdtu.website.entity;

import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(value = "temp_words")
public class TempWords {
    private int id;
    private String word;
    private String sw;
    private String phonetic;
    private String definition;
    private String translation;
    private String exchange;
    private String pos;
    private int bnc;
    private int frq;
    private String tag;
    private short oxford;
    private short collins;
}
