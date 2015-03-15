package bingyan.net.demondict;


import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import db.MyWordList;
import util.GetFromDB;

/**
 * Created by Demon on 2015/2/8.
 * 我的单词本
 *
 */
public class TabMyDict extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private SearchView searchView;
    private ListView listView;
    private MainActivity mainActivity;
    private db.MyWordList myWordList;
    private Boolean isClickable;
    private ImageButton refreshButton;


    public static TabMyDict newInstance(int sectionNumber) {
        TabMyDict fragment = new TabMyDict();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void  init(View v){
        searchView = (SearchView) v.findViewById(R.id.searchView);
        listView = (ListView) v.findViewById(R.id.listView);
        mainActivity = (MainActivity) getActivity();
        myWordList = MyWordList.getInstance(mainActivity);
        refreshButton = (ImageButton) v.findViewById(R.id.button_refresh);
        refreshButton.setBackground(v.getBackground());
        List<String> stringList= GetFromDB.getWords(myWordList);
        ArrayAdapter<String> adapter;
        if (stringList == null){
            stringList = new ArrayList<>();
            stringList.add("单词本中还没有单词，去添加几个吧");
            adapter = new ArrayAdapter<>(mainActivity,
                    android.R.layout.simple_list_item_1, stringList);
            isClickable = false;
        }
        else {
            adapter= new ArrayAdapter<>(mainActivity,
                    android.R.layout.simple_list_item_1, stringList);
            isClickable = true;
        }
        listView.setAdapter(adapter);

        //设置该SearchView默认是否自动缩小为图标
        searchView.setIconifiedByDefault(true);
        //设置该SearchView显示搜索按钮
        searchView.setSubmitButtonEnabled(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.page_3,container,false);
        init(v);
        setListener();
        return v;
    }


    //刷新界面
    public void refresh(){
        ArrayAdapter<String> adapter;
        List<String> stringList = GetFromDB.getWords(myWordList);
        if (stringList == null){
            stringList = new ArrayList<>();
            stringList.add("单词本中还没有单词，去添加几个吧");
            adapter = new ArrayAdapter<>(mainActivity,
                    android.R.layout.simple_list_item_1, stringList);
            isClickable = false;
        }
        else {
            adapter= new ArrayAdapter<>(mainActivity,
                    android.R.layout.simple_list_item_1, stringList);
            isClickable = true;
        }
        listView.setAdapter(adapter);
    }

    //设置监听器
    public void setListener(){
        //为该SearchView组件设置事件监听器
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText))
                    listView.clearTextFilter();
                else
                    listView.setFilterText(newText);
                return true;
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(isClickable){
                    String mEnglish = (String) parent.getItemAtPosition(position);
                    Intent intent = new Intent(mainActivity,
                            bingyan.net.demondict.MyWordList.class);
                    intent.putExtra("mEnglish",mEnglish);
                    startActivity(intent);
                    listView.clearTextFilter();
                }
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
                listView.clearTextFilter();
                Toast.makeText(mainActivity,
                        "刷新成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onResume(){
        super.onResume();
        refresh();
    }
}

