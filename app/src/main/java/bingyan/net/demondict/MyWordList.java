package bingyan.net.demondict;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;
import db.WordListOpenHelper;
import model.Word;

public class MyWordList extends ActionBarActivity {
    private TextView mWord;
    private TextView mPhonetic;
    private TextView mTrans;
    private ImageButton mDelete;
    private ImageButton mTTS;
    private db.MyWordList myWordList;
    private TextToSpeech toSpeech;

    //初始化各控件
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void init(){
        mWord = (TextView) findViewById(R.id.text_My_Word);
        mPhonetic = (TextView) findViewById(R.id.text_Phonetic_My);
        mTrans = (TextView) findViewById(R.id.text_Trans_My);
        mDelete = (ImageButton) findViewById(R.id.remove);
        mTTS = (ImageButton) findViewById(R.id.TTS_My);
        myWordList = db.MyWordList.getInstance(this);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        //初始化TTS引擎
        toSpeech = new TextToSpeech(this,new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status ==TextToSpeech.SUCCESS){
                    int result = toSpeech.setLanguage(Locale.US);
                    if (result != TextToSpeech.LANG_AVAILABLE &&
                            result!= TextToSpeech.LANG_COUNTRY_AVAILABLE)
                        Toast.makeText(MyWordList.this, "TTS不支持这种语言的朗读",
                                Toast.LENGTH_SHORT).show();
                }
            }
        });
        mDelete.setBackground(getWindow().getDecorView().
                findViewById(R.id.my_WordList).getBackground());
        mTTS.setBackground(getWindow().getDecorView().
                findViewById(R.id.my_WordList).getBackground());

        //添加左箭头导航
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_word_list);
        Intent intent = getIntent();
        String mEnglish = intent.getStringExtra("mEnglish");
        init();
        Word word = myWordList.loadWord(mEnglish,
                WordListOpenHelper.TABLE_NAME_MWL);

        mWord.setText(word.getWord());
        mPhonetic.setText(word.getPhonetic());
        mTrans.setText(word.getTrans());
        setListener();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if(item.getItemId() == android.R.id.home)
            finish();//导航键直接返回上层Activity
        return super.onOptionsItemSelected(item);
    }

    //为控件绑定监听器
    public void setListener(){
        //语音
        mTTS.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                toSpeech.speak(mWord.getText().toString(),
                        TextToSpeech.QUEUE_ADD,null);
            }
        });
        //从单词本中移除
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = mWord.getText().toString();
                startDialog(s);

            }
        });
    }

    //开启警告对话框
    public  void  startDialog(String s){
        AlertDialog.Builder dialog = new AlertDialog.Builder
                (MyWordList.this);
        dialog.setTitle("确认");
        dialog.setMessage("你确定要将" + s + "移出你的单词本吗?");
        dialog.setCancelable(false);
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myWordList.delete(WordListOpenHelper.TABLE_NAME_MWL,
                        mWord.getText().toString());
                Toast.makeText(MyWordList.this, "单词" + mWord.getText().toString()
                        + "已移出你的单词本", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        toSpeech.shutdown();
    }



}
