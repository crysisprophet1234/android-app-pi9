package com.example.requirements_manager.application;

import androidx.appcompat.app.AppCompatActivity;

import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.requirements_manager.R;

public class DocumentationActivity extends AppCompatActivity {

    private WebView documentationWv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documentation);

        documentationWv = findViewById(R.id.docWebView);
        documentationWv.getSettings().setJavaScriptEnabled(true);
        documentationWv.getSettings().setLoadsImagesAutomatically(true);
        documentationWv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        documentationWv.clearCache(true);


        documentationWv.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });


        String url = getIntent().getStringExtra("docUrl");
        documentationWv.loadUrl(url);
    }

    @Override
    protected void onDestroy() {
        if (documentationWv != null) {
            documentationWv.destroy();
        }
        super.onDestroy();
    }

}