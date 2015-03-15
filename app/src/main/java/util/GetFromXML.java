package util;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParser;
import java.util.ArrayList;
import java.util.List;
import db.WordList;
import model.Word;
import db.WordListOpenHelper;


/**
 * Created by Demon on 2015/2/3
 * 从XML文件中解析，返回List<Word>
 * 第一个版本是首次启动从XML文件中解析数据并导入数据库，但这样第一次启动较慢
 * 第二个版本弃用这个类，首次启动直接将数据库文件添加到用户的data目录下，速度较快，并成为以后启动的基础
 */
public class GetFromXML {

    public static List<String> getWords
            (Context context,XmlResourceParser xrp, WordList wordList){

        List<String> mEnglish = new ArrayList<>();

        int word_id =0;

        try{
            Word word = null;
            Log.d("a","start");
            while(XmlPullParser.END_DOCUMENT != xrp.getEventType()) {

                if (XmlPullParser.START_TAG == xrp.getEventType()) {

                    if ("item".equals(xrp.getName()) )
                        word = new Word();

                    else if (WordListOpenHelper.WORD.
                            equals(xrp.getName())) {
                        assert word != null;
                        word.setWord(xrp.nextText());

                    }
                    else if (WordListOpenHelper.TRANS.
                            equals(xrp.getName())) {
                        assert word != null;
                        word.setTrans(xrp.nextText());
                    }
                    else if (WordListOpenHelper.PHONETIC
                            .equals(xrp.getName())) {
                        assert word != null;
                        word.setPhonetic(xrp.nextText());
                    }
                    else if (WordListOpenHelper.TAGS
                            .equals(xrp.getName())) {
                        assert word != null;
                        word.setTags(xrp.nextText());
                    }

                }
                if (XmlPullParser.END_TAG == xrp.getEventType()) {

                    if ("item".equals(xrp.getName())) {
                        assert word != null;
                        word.setId(word_id);
                        word.setUseTimes(0);
                        wordList.insertWord(WordListOpenHelper.TABLE_NAME,word);
                        word_id++;
                        mEnglish.add(word.getWord());
                        word = null;
                    }
                }
                xrp.next();
            }
            Log.d("a","end");
            return mEnglish;

        }catch (Exception e ){
            e.printStackTrace();
            Toast.makeText(context, "获取失败",
                    Toast.LENGTH_SHORT).show();
            return  null;
        }
    }
}
