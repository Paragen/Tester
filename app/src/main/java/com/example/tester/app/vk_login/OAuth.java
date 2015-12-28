package com.example.tester.app.vk_login;


import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.example.tester.app.R;

import java.net.URL;


public class OAuth {
    public static String token;
    public static  String user_id;
    static final String VK_URI = "https://api.vk.com/method/";
    private static final String LOG_TAG = "OAuth";
    static String getOAuthUrl(Context context) {
        StringBuilder url = new StringBuilder();
        url.append(context.getString(R.string.base_oauth_url));
        url.append("?client_id=").append(context.getString(R.string.app_id));
        url.append("&redirect_uri=").append(context.getString(R.string.redirect_url));
        url.append("&display=mobile").append("&scope=wall").append("&response_type=token").append("&v=5.42");
        return url.toString();
    }

    public static boolean publish(String text) {
        new Thread(new PostRunnable(text)).start();
        return true;
    }
}
