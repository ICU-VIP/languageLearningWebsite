package org.cdtu.website.common;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Db;
import org.apache.ibatis.cursor.Cursor;
import org.cdtu.website.entity.Words;
import org.cdtu.website.entity.WordsSet;
import org.cdtu.website.entity.table.WordsSetTableDef;
import org.cdtu.website.entity.table.WordsTableDef;
import org.cdtu.website.mapper.WordsMapper;
import org.cdtu.website.mapper.WordsSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


@Component
public class TempUtils {
    private final HashMap<String, Integer> tagMap = new HashMap<>();
    private final String[] OFFSETSTR = {"zk", "gk", "cet4","cet6","ky", "toefl", "ielts", "gre", "oxford"};


    WordsMapper wordsMapper;

    @Autowired
    public void setWordsMapper(WordsMapper wordsMapper) {
        this.wordsMapper = wordsMapper;
    }

    WordsSetMapper wordsSetMapper;

    @Autowired
    public void setWordsSetMapper(WordsSetMapper wordsSetMapper) {
        this.wordsSetMapper = wordsSetMapper;
    }

    public void updateAllWordsSet(){
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(WordsTableDef.WORDS.ID, WordsTableDef.WORDS.TYPE);
        Db.tx(() ->{
            Cursor<Words> words = wordsMapper.selectCursorByQuery(queryWrapper);
            for(Words word:words){
                long k = word.getType();
                short offset = 0;
                while(k > 0){
                    if((k & 1) == 1){
                        QueryWrapper
                                queryWrapper1 = QueryWrapper.create()
                                .select().where(WordsSetTableDef.WORDS_SET.OFFSET.eq(offset));
                        WordsSet wordsSet = wordsSetMapper.selectOneByQuery(queryWrapper1);
                        Byte[] wordsBitMap = wordsSet.getWordsBitmap();
                        /*int a = word.getId() / 8;
                        int b =  word.getId() % 8;
                        wordsBitMap[a] |= (byte) (1 << b);*/
                        BitMapUtil.SetByOffset(wordsBitMap, word.getId());
                        wordsSet.setWordsBitmap(wordsBitMap);
                        wordsSetMapper.update(wordsSet);

                    }
                    offset++;
                    k >>= 1;
                }

            }
            return true;
        });

    }
}
