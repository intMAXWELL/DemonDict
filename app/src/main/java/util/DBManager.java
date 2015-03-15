package util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import bingyan.net.demondict.R;

/**
 * Created by Demon on 2015/2/10.
 * 该类主要用于首次将资源文件导入用户数据库
 */
public class DBManager {
    //保存的数据库文件名
    public static final String DB_NAME = "WordList";
    public static final String PACKAGE_NAME = "bingyan.net.demondict";
    //在手机里存放数据库的位置
    public static final String DB_PATH = "/data"
            + Environment.getDataDirectory().getAbsolutePath() + "/"
            + PACKAGE_NAME;
    private Context context;

    public DBManager(Context context) {
        this.context = context;
    }

    public SQLiteDatabase openDatabase(String dbfile) {
        try {
            Log.d("file",DB_PATH);
            if (!(new File(dbfile).exists())) {//判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
                InputStream is = this.context.getResources().openRawResource(
                        R.raw.wordlist); //欲导入的数据库
                FileOutputStream fos = new FileOutputStream(dbfile);
                int BUFFER_SIZE = 400000;
                byte[] buffer = new byte[BUFFER_SIZE];
                int count;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }

            new UseCount().setFlag(context,true);
            return SQLiteDatabase.openOrCreateDatabase(dbfile,
                    null);
        } catch (IOException e) {
            //Log.e("Database", "IO exception");
            e.printStackTrace();
        }
        new  UseCount().setFlag(context,false);
        return null;
    }

}

