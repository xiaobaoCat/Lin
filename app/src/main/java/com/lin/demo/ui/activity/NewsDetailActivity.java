package com.lin.demo.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.lin.demo.R;

public class NewsDetailActivity extends BaseActivity implements View.OnClickListener{
    private NewsDetailActivity mContext;
    private WebView webView;
    private String  url="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        initItentData();
        initWebView();
    }

    private void initItentData() {
        Intent intent=getIntent();
        String url=intent.getStringExtra("url");
        if (url!=null){
            this.url=url;
        }
    }

    private void initWebView() {
        webView = (WebView) findViewById(R.id.webview);
        webView.setLongClickable(true);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //WebView屏幕自适应
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        webSettings.setTextSize(WebSettings.TextSize.NORMAL);

        webSettings.setBlockNetworkImage(false);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.loadUrl(url);
        //防止自动的打开系统内置的浏览器。
        webView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        //获取webview内页面的加载进度：
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //get the newProgress and refresh progress bar
            }
        });
        //当前webview正在加载的页面的title
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {

            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}