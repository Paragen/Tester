package com.example.tester.app.vk_login;


import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.example.tester.app.MainActivity;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends Activity {

    final String LOG_TAG = "Login Activity";

    protected void onCreate(Bundle saveInstance) {
        super.onCreate(saveInstance);
        boolean flag = getIntent().getBooleanExtra("login", true);
        if (!flag) {
            android.webkit.CookieManager.getInstance().removeAllCookie();
            OAuth.token = null;
            OAuth.user_id = null;
            setResult(RESULT_OK);
            finish();
        }
        WebView webView = new WebView(this);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                if (url.startsWith(OAuth.REDIRECT_URL)) {
                    parseResult(url);
                    return true;
                }
                return false;
            }
        });
        setContentView(webView);
        webView.loadUrl(OAuth.getOAuthUrl());
    }


    void parseResult(String url) {
        Uri uri = Uri.parse(url);
        String fragment = uri.getFragment();
        int from = 0, to;
        while (from < fragment.length() && (to = fragment.indexOf('=', from)) != -1) {
            String key = fragment.substring(from, to);
            from = fragment.indexOf('&', to);
            if (from == -1) {
                from = fragment.length();
            }
            String value = fragment.substring(to + 1, from);
            from++;
            if (key.equals("error")) {
                Log.d(LOG_TAG, fragment);
                setResult(RESULT_CANCELED);
                finish();

            } else if (key.equals("access_token")) {
                OAuth.token = value;

            } else if (key.equals("user_id")) {
                OAuth.user_id = value;

            }
        }
        Log.d(LOG_TAG, "Success");
        setResult(RESULT_OK);
        finish();
    }
    public static boolean publish(String text) {
        new Thread(new PostRunnable(text)).start();
        return true;
    }

    public static boolean getCurrentName(MainActivity activity) {
        new AsyncLoader(activity).execute();
        return true;
    }

    private static class AsyncLoader extends AsyncTask<Void,String,String> {
        MainActivity activity;

        AsyncLoader(MainActivity activity) {
            this.activity = activity;
        }
        @Override
        protected String doInBackground(Void... voids) {
            String answer = null;
            try {
                Uri uri = Uri.parse(OAuth.VK_URI).buildUpon().appendEncodedPath("users.get").appendQueryParameter("user_ids", OAuth.user_id).build();
                JsonReader reader = new JsonReader(new InputStreamReader(((HttpURLConnection)new URL(uri.toString()).openConnection()).getInputStream()));
                String first_name = null, last_name = null;
                reader.beginObject();
                if(reader.nextName().equals("error")) {
                    Log.d("----------", reader.nextString());
                }
                reader.beginArray();
                reader.beginObject();
                while (first_name == null || last_name == null) {
                    String name = reader.nextName();
                    if (name.equals("first_name")) {
                        first_name = reader.nextString();
                    } else if (name.equals("last_name")) {
                        last_name = reader.nextString();
                    } else {
                        reader.skipValue();
                    }
                }
                reader.close();
                answer = first_name + " " + last_name;
            } catch (Exception e) {
                Log.d("Async Loader", e.getMessage());
            }
            return answer;
        }

        @Override
        protected void onPostExecute(String answer) {
            activity.textView.setText("Hello, " + answer);
        }
    }
}
