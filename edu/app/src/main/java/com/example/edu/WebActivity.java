package com.example.edu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ZoomControls;

/**
 * Created by 扶摇 on 2017/6/24.
 */

public class WebActivity extends AppCompatActivity {

    private WebView wv_web;
private ZoomControls zoomControls;
    private int size = 25;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity);



        this.zoomControls = (ZoomControls) findViewById(R.id.zoomcontrols);
        wv_web = (WebView) findViewById(R.id.wv_web);

        wv_web.getSettings().setJavaScriptEnabled(true);
// 设置可以支持缩放
        wv_web.getSettings().setSupportZoom(true);
// 设置出现缩放工具
        wv_web.getSettings().setBuiltInZoomControls(true);
        wv_web.getSettings().setDefaultFontSize(25);
//扩大比例的缩放
        wv_web.getSettings().setUseWideViewPort(true);
//自适应屏幕
        wv_web.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wv_web.getSettings().setLoadWithOverviewMode(true);

        this.zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivity.this.size  = size + 5;
                WebActivity.this.wv_web.getSettings().setTextZoom(size);
            }
        });
        this.zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivity.this.size  = size - 5;
                WebActivity.this.wv_web.getSettings().setTextZoom(size);
            }
        });
        this.zoomControls.show();

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        wv_web.loadUrl(url);

        wv_web.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            { // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
