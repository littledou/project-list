package com.readface.cafe.anim;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ScrollView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.readface.cafe.utils.BitmapCache;
import com.readface.cafe.utils.FaceUtil;
import com.readface.cafe.utils.VolleyHelper;

/**
 * Created by mac on 15/12/1.
 */
public class GuideActivity extends BaseActivity {
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);

        mWebView = (WebView) findViewById(R.id.webview);

        WebSettings setting = mWebView.getSettings();
        setSettings(setting);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url1) {
                return super.shouldOverrideUrlLoading(view, url1);
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });
        mWebView.removeJavascriptInterface("searchBoxJavaBredge_");
        mWebView.loadUrl("http://assets.fashionyear.net/robot_doudou_guide.html");
    }

    @SuppressLint("NewApi")
    private void setSettings(WebSettings setting) {
        setting.setJavaScriptEnabled(true);
        setting.setBuiltInZoomControls(true);
        setting.setDisplayZoomControls(false);
        setting.setSupportZoom(true);

        setting.setDomStorageEnabled(true);
        setting.setDatabaseEnabled(true);
        // 全屏显示
        setting.setLoadWithOverviewMode(true);
        setting.setUseWideViewPort(true);
    }
}
