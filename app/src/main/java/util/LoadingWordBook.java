package util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.util.List;

import bingyan.net.demondict.R;
import db.WordList;
import model.DropDownAdapter;


/**
 * Created by Demon on 2015/2/4.
 * 第一个版本：后台执行XML解析并把解析结果存入数据库
 * 第二个版本：数据库文件已准备就绪，直接读取，然后为autoCompleteTextView设置adapter
 */
public class LoadingWordBook extends AsyncTask<Void,Integer,List<String>> {
    private ProgressDialog progressDialog;
    private Context context;
    private WordList wordList;
    private AutoCompleteTextView autoCompleteTextView;

    public LoadingWordBook(ProgressDialog progressDialog,
                           AutoCompleteTextView autoCompleteTextView,
                           Context context, WordList wordList)
    {
        this.progressDialog = progressDialog;
        this.context = context;
        this.wordList = wordList;
        this.autoCompleteTextView = autoCompleteTextView;
    }

    //线程开启前更新UI，屏蔽操作
    @Override
    protected void onPreExecute(){
        progressDialog.show();
    }

    //子线程后台任务
    //获取英文单词集合
    @Override
    protected List<String> doInBackground(Void[] params) {

        return GetFromDB.getWords(wordList);

    }

    //后台任务执行时UI界面的实时更新
    @Override
    protected void onProgressUpdate(Integer... values){

        //progressDialog.setMessage("Loading" + values[0] + "%");

    }

    //子线程结束后
    @Override
    protected void onPostExecute(List<String> mEnglish ){

        if (null != mEnglish) {
            DropDownAdapter dropDownAdapter
                    =new DropDownAdapter(context, R.layout.drop_down,
                    mEnglish);
            autoCompleteTextView.setAdapter(dropDownAdapter);

            Toast.makeText(context,"加载成功，共有"+mEnglish.size()+"个单词成功导入！"
                   ,Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(context,"加载失败！",Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }
}
