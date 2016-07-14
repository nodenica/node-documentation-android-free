package com.mc.nad.ui.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mc.nad.R;
import com.mc.nad.api.DownloadDocs;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ModuleActivity extends BaseActivity {

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_NAME = "name";

    @BindView(R.id.webView)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);

        ButterKnife.bind(this);

        // get extras
        Bundle bundle = getIntent().getExtras();
        String title = bundle.getString(EXTRA_TITLE);
        String name = bundle.getString(EXTRA_NAME);

        // set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

       showProgressDialog(R.string.progress_loading_module_page);

        // init webView
        initWebView(name);

        // init ads
        initAds();
    }

    private void initWebView(String name) {
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    hideProgressDialog();
                }
            }
        });

        webView.loadUrl(DownloadDocs.getFilePathToWebView(name));

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    private void initAds() {
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
