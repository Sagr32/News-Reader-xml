package com.sagr.newsreader.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sagr.newsreader.R;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Intent intent = getIntent();
        if (null != intent) {
            String url = intent.getStringExtra("url");
            if (null != url) {
                webView = findViewById(R.id.webView);
                webView.setWebViewClient(new WebViewClient());
                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl(url);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            getObbDir();
        }else {
            super.onBackPressed();
        }
    }

}

