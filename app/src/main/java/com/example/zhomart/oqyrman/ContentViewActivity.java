package com.example.zhomart.oqyrman;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class ContentViewActivity extends Activity {
    WebView webView;
    @SuppressLint("SetJavaScriptEnabled")
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_view);
        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        String displayString = getIntent().getStringExtra("display");
        if (displayString != null) {
            webView.loadData(displayString, "text/html", "utf-8");
        }
    }
}