package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Demon on 2015/2/5.
 * 建立WordList表
 * 各参数值与word类中对应
 * TABLE_NAME---------表名
 * CREATE_INFO-----建表语句
 */
public class WordListOpenHelper extends SQLiteOpenHelper {

    public static final String WORD = "word";
    public static final String ID = "id";
    public static final String TRANS = "trans";
    public static final String PHONETIC = "phonetic";
    public static final String TAGS = "tags";
    public static final String USETIMES = "usetimes";
    public static final String TABLE_NAME = "Wordlist";
    public static final String TABLE_NAME_MWL = "MyWordlist";
    public static final String CREATE_INFO_MWL ="create table "
            + TABLE_NAME_MWL+"("
            + ID+" integer primary key autoincrement,"
            + WORD + " text, "
            + TRANS+ " text,"
            + PHONETIC + " text,"
            + TAGS + " text,"
            + USETIMES+" integer)";
    /*public static final String CREATE_INFO ="create table "
            + TABLE_NAME+"("
            + ID +" integer primary key autoincrement,"
            + WORD + " text, "
            + TRANS+ " text,"
            + PHONETIC + " text,"
            + TAGS + " text,"
            + USETIMES+" integer)";*/


    public WordListOpenHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //Log.d("Create", CREATE_INFO_MWL);
        db.execSQL(CREATE_INFO_MWL);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
