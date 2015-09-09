package com.mc.nad;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.net.MalformedURLException;

import android.os.Build;

import java.net.URL;

import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;

import nad.mc.com.nodejsdocumentationfree.R;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    /**
     * The view to show the ad.
     */
    private AdView adView;
    private AdRequest adRequest;
    private String deviceid;

    private static Toolbar mToolbar;


    /* Your ad unit id. Replace with your actual ad unit id. */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // toolbar
        setUpToolbar();

        // admob
        adMobInit();


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
                getSupportActionBar().setTitle(view.getTitle());

                URL urls;
                try {
                    urls = new URL(webView.getUrl());
                    // get file name from url
                    String fileName = urls.getPath().substring(
                            urls.getPath().lastIndexOf("/") + 1);
                    if (fileName != "index.html") {
                        // show back on ActionBar
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    } else {
                        // hide back on ActionBar
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    }
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });

    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            // Hide back on ActionBar
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        setToolbarColors(R.color.color_primary, R.color.color_primary_dark);
    }

    public void setToolbarColors(int primaryColor, int primaryDarkColor) {
        mToolbar.setBackgroundColor(getResources().getColor(primaryColor));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // set colorPrimaryDark
            getWindow().setStatusBarColor(getResources().getColor(primaryDarkColor));
            getWindow().setNavigationBarColor(getResources().getColor(primaryColor));
        }
    }

    private void adMobInit() {
        // Create an ad.
        adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(getString(R.string.admob_key));

        // Add the AdView to the view hierarchy. The view will have no size
        // until the ad is loaded.
        LinearLayout layout = (LinearLayout) findViewById(R.id.ad);
        layout.removeAllViews();

        if (isConnected()) {
            layout.setVisibility(View.VISIBLE);
            layout.addView(adView);

            final TelephonyManager tm = (TelephonyManager) getBaseContext()
                    .getSystemService(Context.TELEPHONY_SERVICE);

            deviceid = tm.getDeviceId();

            // Create an ad request. Check logcat output for the hashed device
            // ID to
            // get test ads on a physical device.
            adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice(deviceid).build();

            // Start loading the ad in the background.
            adView.loadAd(adRequest);
        } else {
            layout.setVisibility(View.GONE);
        }
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = false;
        if (activeNetwork != null)
            isConnected = true;
        return isConnected;
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
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    /**
     * Called before the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        adMobInit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
}
