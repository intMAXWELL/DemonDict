package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

import model.Word;
import util.DBManager;

/**
 * Created by Demon on 2015/2/5.
 * 通过调用getInstance获取WordList实例
 *
 */
public class WordList {
    private static WordList wordList;
    private SQLiteDatabase db;

    //导入成功则直接打开本地数据库
    //导入失败或未导入则导入并打开资源文件@R.raw.wordlist
    private WordList(Boolean isImported,Context context) {
        if(isImported)
            db = SQLiteDatabase.openOrCreateDatabase(DBManager.DB_PATH + "/"
                    + DBManager.DB_NAME,null);
        else db = new DBManager(context).openDatabase
                (DBManager.DB_PATH + "/" + DBManager.DB_NAME);
    }

    public synchronized static WordList getInstance
            (Boolean isImPorted,Context context) {
        if (null == wordList) {
            wordList = new WordList(isImPorted,context);
        }
        return wordList;
    }


    /*从数据库中查找对应的单词
     *传入参数：
     *       String:english---------单词英文
     *       String:tb_name------------表名
     *返回类型：
     *       Word:word------------单词类实体
     */
    public Word loadWord(String english,String tb_name){
        Word word = null  ;
        Cursor cursor = db.query(tb_name,null,WordListOpenHelper.WORD+"=?",
                new String[]{english},null,null,null);
        if (cursor.moveToFirst()){
            do {
                word = new Word();
                word.setId(cursor.getInt(0));
                word.setWord(cursor.getString(cursor.getColumnIndex
                        (WordListOpenHelper.WORD)));
                word.setTrans(cursor.getString(cursor.getColumnIndex
                        (WordListOpenHelper.TRANS)));
                word.setPhonetic(cursor.getString(cursor.getColumnIndex
                        (WordListOpenHelper.PHONETIC)));
                word.setTags(cursor.getString(cursor.getColumnIndex
                        (WordListOpenHelper.TAGS)));
                word.setUseTimes(cursor.getInt(5));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return word;
    }


    /*从数据库中查找所有单词
     *传入参数：
     *       String：tb_name--------------表名
     *返回类型：
     *       ArrayList<word>:words---全单词集合
     */
    public List<Word> loadWords(String tb_name){
        List<Word> words = null ;
        Cursor cursor = db.query(tb_name,null,null,null,null,null,
                WordListOpenHelper.USETIMES+" desc");
        if(cursor.moveToFirst()){
            words = new ArrayList<>();
            do {
                Word word = new Word();
                word.setId(cursor.getInt(0));
                word.setWord(cursor.getString(cursor.getColumnIndex
                        (WordListOpenHelper.WORD)));
                word.setTrans(cursor.getString(cursor.getColumnIndex
                        (WordListOpenHelper.TRANS)));
                word.setPhonetic(cursor.getString(cursor.getColumnIndex
                        (WordListOpenHelper.PHONETIC)));
                word.setTags(cursor.getString(cursor.getColumnIndex
                        (WordListOpenHelper.TAGS)));
                word.setUseTimes(cursor.getInt(5));
                words.add(word);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return words;
    }


    /*更新单词的搜索次数
     *传入参数：
     *       String:tb_name----------------------表名
     *       Word:word-----------------需要被更新的单词
     *返回类型：
     *       int:num-----the number of rows affected
     */
    public int update(String tb_name,Word word){
        ContentValues values = new ContentValues();
        values.put(WordListOpenHelper.USETIMES,word.getUseTimes()+1);
        return db.update(tb_name,values,
                WordListOpenHelper.WORD+"=?",new String[]{word.getWord()});
    }

    /*向数据库中插入一个单词
    *传入参数:
    *       String:tb_name----------------------表名
    *       Word：word--------------------------单词
    *返回类型：
    *       Boolean:bool---------------------是否成功
    */
    public Boolean  insertWord(String tb_name,Word word){
        Boolean bool = false;
        if (null != word){
            ContentValues values = new ContentValues();
            values.put(WordListOpenHelper.WORD,word.getWord());
            values.put(WordListOpenHelper.TRANS,word.getTrans());
            values.put(WordListOpenHelper.PHONETIC,word.getPhonetic());
            values.put(WordListOpenHelper.TAGS,word.getTags());
            values.put(WordListOpenHelper.USETIMES,word.getUseTimes());
            if (-1 != db.insert(tb_name,null,values))
                bool = true;
        }
        return bool;
    }

    /*向数据库中插入批量数据
     *传入参数:
     *       String:tb_name----------------------表名
     *       List<Word>:words-----------------单词集合
     *返回类型：
     *       int:num-------------成功插入的单词个数
     */
    //未使用
    public int  insertWords(String tb_name,List<Word> words){
        int num = 0;
        for (Word word:words){
            if (null != word){
                ContentValues values = new ContentValues();
                values.put(WordListOpenHelper.WORD,word.getWord());
                values.put(WordListOpenHelper.TRANS,word.getTrans());
                values.put(WordListOpenHelper.PHONETIC,word.getPhonetic());
                values.put(WordListOpenHelper.TAGS,word.getTags());
                values.put(WordListOpenHelper.USETIMES,word.getUseTimes());
                if (-1 != db.insert(tb_name,null,values))
                    num++;
            }
        }
        return num;
    }

    public void close(){
        db.close();
    }

}
