package bingyan.net.demondict;


import android.annotation.TargetApi;
import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by Demon on 2015/2/8.
 * 网页查询
 */
@SuppressWarnings("deprecation")
public class TabWebDict  extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private String rootStr = "http://dict.youdao.com/m/search?keyfrom=dict.mindex&q=";
    private WebView webView;


    public static TabWebDict newInstance(int sectionNumber) {
        TabWebDict fragment = new TabWebDict();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_2, container, false);

        //初始化控件
        final EditText editText = (EditText) view.
                findViewById(R.id.editText);
        final ImageButton btnSearch = (ImageButton) view.
                findViewById(R.id.searchButton2);
        btnSearch.setBackground(view.getBackground());
        final ImageButton btnBack = (ImageButton) view.
                findViewById(R.id.backButton);
        btnBack.setBackground(view.getBackground());
        final MainActivity mainActivity = (MainActivity) getActivity();
        final android.support.v7.app.ActionBar actionBar =
                mainActivity.getSupportActionBar();

        webView = (WebView) view.
                findViewById(R.id.webView);

        if(webView == null)
            Toast.makeText(mainActivity,"浏览器初始化失败",Toast.LENGTH_SHORT).show();

        assert webView != null;
        WebSettings webSettings = webView.getSettings();
        //支持js
        webSettings.setJavaScriptEnabled(true);
        //可按任意比缩放
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        //支持缩放
        webSettings.setBuiltInZoomControls(true);
        //设置缓存，离线使用
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setAppCacheMaxSize(1024*1024*8);


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = editText.getText().toString();
                url = url.trim();
                if (0 == url.length())
                    Toast.makeText(mainActivity, R.string.tips_2, Toast.LENGTH_SHORT).show();
                else {
                    String strURL = rootStr + url;
                    webView.setWebViewClient(new WebViewClient() {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String
                                url) {
                            view.loadUrl(url);
                            return true;
                        }
                    });
                    webView.loadUrl(strURL);
                    actionBar.hide();

                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack())
                    webView.goBack();
                    else actionBar.show();
            }
        });
        return view;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
