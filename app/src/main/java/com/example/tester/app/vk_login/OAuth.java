package com.example.tester.app.vk_login;


import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.example.tester.app.R;

import java.net.URL;


public class OAuth {
    public static String token;
    public static  String user_id;
    public static final String VK_URI = "https://api.vk.com/method/", REDIRECT_URL = "https://oauth.vk.com/blank.html",
        APP_ID = "5204789", BASE_URL = "https://oauth.vk.com/authorize";
    private static final String LOG_TAG = "OAuth";
    public static String getOAuthUrl() {
        StringBuilder url = new StringBuilder();
        url.append(BASE_URL);
        url.append("?client_id=").append(APP_ID);
        url.append("&redirect_uri=").append(REDIRECT_URL);
        url.append("&display=mobile").append("&scope=wall").append("&response_type=token").append("&v=5.42");
        return url.toString();
    }

}
