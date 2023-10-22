package com.webapp.oasis;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;


public class WebviewActivity extends AppCompatActivity {

    String getlink;
    WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.parseColor("#4352D3"));
//        }


        webView = (WebView) findViewById(R.id.webView);

        getlink = getIntent().getStringExtra("url");
        if (!getlink.equals("https://oasisro.web.app/")){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getlink));

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "No Browser found", Toast.LENGTH_SHORT).show();
            }
        }else {
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient());
            webView.setWebChromeClient(new WebChromeClient());
            webView.loadUrl(getlink);
            final Dialog customDialog = new Dialog(WebviewActivity.this);
            customDialog.setContentView(R.layout.custom_dialog);
            customDialog.show();

            webView.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageCommitVisible(WebView view, String url) {
                    super.onPageCommitVisible(view, url);
                    customDialog.dismiss();
                }
            });

            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                    return super.onJsAlert(view, url, message, result);
                }
            });

            checkConnection();
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.isFocused() && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    public void checkConnection() {
        if (isOnline()) {

        } else {
            networkmain();
        }
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void networkmain() {
        final Dialog dialogs = new Dialog(WebviewActivity.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setContentView(R.layout.networkdialog);
        dialogs.setCanceledOnTouchOutside(false);
        Button done = dialogs.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                webView.loadUrl(getlink);
                final Dialog customDialog = new Dialog(WebviewActivity.this);
                customDialog.setContentView(R.layout.custom_dialog);
                customDialog.show();

                webView.setWebViewClient(new WebViewClient() {

                    @Override
                    public void onPageCommitVisible(WebView view, String url) {
                        super.onPageCommitVisible(view, url);
                        customDialog.dismiss();
                    }
                });
            }
        });
        dialogs.show();
    }

}