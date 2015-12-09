package com.mc.nad;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.net.MalformedURLException;

import android.os.Build;

import java.net.URL;

import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;

import nad.mc.com.nodejsdocumentationfree.R;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private static Toolbar toolbar;


    /* Your ad unit id. Replace with your actual ad unit id. */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        setToolbarColors(R.color.color_primary, R.color.color_primary_dark);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // WebView Instance
        webView = (WebView) findViewById(R.id.webView);
        // Enable JAvaScript
        webView.getSettings().setJavaScriptEnabled(true);
        // Load home page
        webView.loadUrl("file:///android_asset/en/index.html");

        // Handler webView client
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                toolbar.setTitle(view.getTitle());

                URL urls;
                try {
                    urls = new URL(webView.getUrl());
                    // get file name from url
                    String fileName = urls.getPath().substring(
                            urls.getPath().lastIndexOf("/") + 1);
                    if (fileName != "index.html") {
                        // show back on ActionBar
                        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
                    } else {
                        // hide back on ActionBar
                        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
                    }
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    public void setToolbarColors(int primaryColor, int primaryDarkColor) {
        toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), primaryColor));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // set colorPrimaryDark
            getWindow().setStatusBarColor(getResources().getColor(primaryDarkColor));
            getWindow().setNavigationBarColor(getResources().getColor(primaryColor));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
                return true;
            case R.id.action_settings:
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.mc.nad.pro"));
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
}
