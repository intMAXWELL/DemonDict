package bingyan.net.demondict;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;
import db.MyWordList;
import db.WordList;
import db.WordListOpenHelper;
import model.Word;
import util.LoadingWordBook;
import util.UseCount;

/**
 * Created by Demon on 2015/2/8.
 * 本地查询
 */
public class TabDict extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private TextToSpeech toSpeech;
    private TextView text_Word;
    private MainActivity activity;
    private TextView text_Trans;
    private TextView text_Phonetic;
    private ImageButton searchButton;
    private ImageButton add_To_MyWordList;
    private ImageButton tts;
    private android.support.v7.app.ActionBar actionBar;
    private AutoCompleteTextView autoCompleteTextView;
    private WordList wordList;
    private db.MyWordList myWordList;

    //初始化各控件
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void init(View view){
        //初始化控件
        activity = (MainActivity)getActivity();
        text_Word = (TextView) view.
                findViewById(R.id.text_Word);
        text_Trans = (TextView) view.
                findViewById(R.id.text_Trans);
        text_Phonetic = (TextView) view.
                findViewById(R.id.text_Phonetic);
        searchButton = (ImageButton) view.
                findViewById(R.id.searchButton1);
        add_To_MyWordList = (ImageButton) view.
                findViewById(R.id.add_to_MyWordList);
        tts = (ImageButton) view.
                findViewById(R.id.TTS);
        actionBar = activity.
                getSupportActionBar();
        autoCompleteTextView = (AutoCompleteTextView) view.
                findViewById(R.id.autoCompleteTextView);

        myWordList = MyWordList.getInstance(activity);
        setVisibility(View.INVISIBLE);

        //初始化TTS引擎
        toSpeech = new TextToSpeech(activity,new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {

                    int result = toSpeech.setLanguage(Locale.US);
                    if (result != TextToSpeech.LANG_AVAILABLE &&
                            result != TextToSpeech.LANG_COUNTRY_AVAILABLE)
                        Toast.makeText(activity, "TTS不支持这种语言的朗读",
                                Toast.LENGTH_SHORT).show();

                }
            }
        });

        searchButton.setBackground(view.getBackground());
        add_To_MyWordList.setBackground(view.getBackground());
        tts.setBackground(view.getBackground());

    }

    //设置某些隐藏控件的可见性
    public void setVisibility(int visibility){
        text_Word.setVisibility(visibility);
        text_Trans.setVisibility(visibility);
        text_Phonetic.setVisibility(visibility);
        add_To_MyWordList.setVisibility(visibility);
        tts.setVisibility(visibility);
    }

    public static TabDict newInstance(int sectionNumber) {
        TabDict fragment = new TabDict();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.page_1,container,false);

        init(view);

        int count = new UseCount().useTimes(activity);
        ProgressDialog progressDialog;
        wordList = WordList.getInstance
                (new UseCount().isImportSucceed(activity),activity);

        //是否是首次启动,首次启动导入资源文件
        //若非首次启动则直接数据库读取，耗时短一些
        if(0 == count ){
            //开启Dialog屏蔽UI
            progressDialog = startPDialog(activity,
                    "首次启动需要从资源文件中导入词库，稍等片刻");
        }
        else {
            //开启Dialog屏蔽UI
            progressDialog = startPDialog(activity,
                    "从数据库读取数据中");
        }

        //子线程执行任务，主要是为autoCompleteTextView设置adapter
        LoadingWordBook task;
        task = new LoadingWordBook(progressDialog,autoCompleteTextView,
                activity,wordList);
        task.execute();

        setListener();

        return view;
    }

    //控件绑定监听器
    public void setListener(){
        //为搜索按钮绑定点击监听器
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Word word;
                word = wordList.loadWord(autoCompleteTextView.getText().toString(),
                        WordListOpenHelper.TABLE_NAME);
                if (null != word) {
                    setVisibility(View.VISIBLE);
                    text_Trans.setText(word.getTrans());
                    text_Word.setText(word.getWord());
                    text_Phonetic.setText(word.getPhonetic());
                    wordList.update(WordListOpenHelper.TABLE_NAME,word);
                    if (myWordList.loadWord(word.getWord(),
                            WordListOpenHelper.TABLE_NAME_MWL)!=null)
                        myWordList.update(WordListOpenHelper.TABLE_NAME_MWL,
                                word);
                }
                else {
                    text_Word.setVisibility(View.VISIBLE);
                    text_Word.setText("未找到该单词！试试网页搜索吧！");
                }
                assert actionBar != null;
                actionBar.show();
            }
        });

        //为搜索框绑定点击监听器
        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert actionBar != null;
                actionBar.hide();
            }
        });

        //为搜索框绑定文本改变监听器
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //为发音按钮绑定监听器
        tts.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                if(!"".equals(text_Word.getText().toString()))
                    toSpeech.speak(text_Word.getText().toString(),
                            TextToSpeech.QUEUE_ADD,null);
            }
        });

        //添加到单词本
        add_To_MyWordList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Word word;
                word = wordList.loadWord(text_Word.getText().toString(),
                        WordListOpenHelper.TABLE_NAME);
                if (word != null){
                    if (myWordList.loadWord(word.getWord(),
                            WordListOpenHelper.TABLE_NAME_MWL)== null)
                    {
                            myWordList.insertWord(WordListOpenHelper.TABLE_NAME_MWL,word);
                            Toast.makeText(activity,
                                    "已将单词"+word.getWord()+"加入单词本",
                                    Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(activity,
                                "单词已存在单词本中",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //开启对话框屏蔽UI界面
    private ProgressDialog startPDialog(Context context,String msg){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(msg);
        return progressDialog;
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        toSpeech.shutdown();
    }
}
