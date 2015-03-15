package util;

import java.util.ArrayList;
import java.util.List;

import db.MyWordList;
import db.WordList;
import db.WordListOpenHelper;
import model.Word;

/**
 * Created by Demon on 2015/2/6.
 * 从数据库中加载数据
 */
public class GetFromDB {

    //从数据库中提取数据
    public static List<String> getWords(WordList wordList){
        List<String> mEnglish = new ArrayList<>();
        List<Word> words =
                wordList.loadWords(WordListOpenHelper.TABLE_NAME);
        for(Word word:words){
            mEnglish.add(word.getWord());
        }
        return mEnglish;
    }

    //从我的单词本获取单词英文集合
    public static List<String> getWords(MyWordList myWordList){
        List<String> mEnglish = null;
        List<Word> words =
                myWordList.loadWords(WordListOpenHelper.TABLE_NAME_MWL);
        if (words != null){
            mEnglish = new ArrayList<>();
            for(Word word:words)
            {
                mEnglish.add(word.getWord());
            }
        }
        return mEnglish;
    }

}
