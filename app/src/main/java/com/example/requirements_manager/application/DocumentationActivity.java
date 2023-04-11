package com.example.requirements_manager.application;

import androidx.appcompat.app.AppCompatActivity;

import android.net.http.SslError;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.requirements_manager.R;

public class DocumentationActivity extends AppCompatActivity {

    private WebView documentationWv;
    private MenuHelper menuHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documentation);

        menuHelper = new MenuHelper(this);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        menuHelper.onCreateOptionsMenu(menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return menuHelper.onOptionsItemSelected(item);
    }
    @Override
    protected void onDestroy() {
        if (documentationWv != null) {
            documentationWv.destroy();
        }
        super.onDestroy();
    }

}