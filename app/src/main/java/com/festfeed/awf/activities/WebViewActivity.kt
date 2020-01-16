package com.festfeed.awf.activities

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.webkit.*
import com.festfeed.awf.R
import com.festfeed.awf.base.BaseActivity
import com.festfeed.awf.utils.click
import com.festfeed.awf.utils.finishDown
import kotlinx.android.synthetic.main.activity_webview.*

class WebViewActivity : BaseActivity() {

    override fun layoutResID() = R.layout.activity_webview

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            window.apply {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                statusBarColor = Color.TRANSPARENT
            }
        } catch (e: Exception) {
        }
        backBtn.click { onBackPressed() }
        //

        webView.settings.builtInZoomControls = false;
        webView.settings.cacheMode = WebSettings.LOAD_DEFAULT;
        webView.settings.domStorageEnabled = true;
        webView.clearHistory();
        webView.clearCache(false);
       // webView.getSettings().setSupportZoom(true);
        webView.settings.useWideViewPort = true;
        webView.settings.loadWithOverviewMode = true;
        webView.clearFormData();
        webView.settings.javaScriptEnabled = true;

        webView.loadUrl(intent.getStringExtra("url"))
        //   webView.loadUrl("https://www.glowgardens.com/saskatoon-christmas/#calendar")
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility = View.GONE
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                progressBar.visibility = View.GONE
            }
        }
    }

    override fun onBackPressed() {
        finishDown()
    }
}