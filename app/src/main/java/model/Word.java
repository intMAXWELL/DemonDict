package model;

/**
 * Created by Demon on 2015/2/5.
 * word---------------------英文
 * tran------------------中文释义
 * id--------------------默认主键
 * tag------------------该单词标签
 * phonetic-------------该单词发音
 * useTimes------ 被使用（搜索）次数
 */
public class Word {
    private String word;
    private String trans;
    private String phonetic;
    private String tags;
    private int id;
    private int useTimes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTrans() {
        return trans;
    }

    public void setTrans(String trans) {
        this.trans = trans;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getUseTimes() {
        return useTimes;
    }

    public void setUseTimes(int useTimes) {
        this.useTimes = useTimes;
    }

    public String getDetails(){
        return ""+id+"\n"+word+"\n"+phonetic+"\n"+trans+"\n"+tags+"\n"+useTimes;
    }

}

