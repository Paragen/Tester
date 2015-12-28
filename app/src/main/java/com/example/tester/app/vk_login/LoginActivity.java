package com.example.tester.app.vk_login;


import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.example.tester.app.R;


public class LoginActivity extends Activity {

    WebView webView;
    final String LOG_TAG = "Login Activity";
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);


        webView = new WebView(this);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                if (url.startsWith(getString(R.string.redirect_url))) {
                    parseResult(url);
                    return true;
                }
                return  false;
            }
        });
        setContentView(webView);
        webView.loadUrl(OAuth.getOAuthUrl(this));
    }

    void parseResult(String url) {
        Uri uri = Uri.parse(url);
        String fragment = uri.getFragment();
        int from = 0, to;
        while (from < fragment.length() &&(to = fragment.indexOf('=',from)) != -1) {
            String key = fragment.substring(from,to);
            from = fragment.indexOf('&',to );
            if (from == -1) {
                from = fragment.length();
            }
            String value = fragment.substring(to + 1, from);
            from++;
            if (key.equals("error")) {
                Log.d(LOG_TAG, fragment);
                finish();

            } else if (key.equals("access_token")) {
                OAuth.token = value;

            } else if (key.equals("user_id")) {
                OAuth.user_id = value;

            }
        }
        Log.d(LOG_TAG,"Success");
        finish();
    }

}
