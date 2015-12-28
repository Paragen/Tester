package com.example.tester.app.vk_login;


import android.net.Uri;
import android.util.Log;

import java.net.URL;

public class PostRunnable implements Runnable {
   String text;
    final  String LOG_TAG = "Post Runnable";
    PostRunnable(String text) {
        this.text = text;
    }


    public void run() {
        if (OAuth.token == null) {
            return;
        }
        Uri uri = Uri.parse(OAuth.VK_URI).buildUpon().appendPath("wall.post").appendQueryParameter("owner_id",OAuth.user_id).appendQueryParameter("message",text).appendQueryParameter("access_token",OAuth.token).build();
        try {
            (new URL(uri.toString())).openConnection().getInputStream().close();

        } catch (Exception e) {
            Log.d(LOG_TAG, "cant do post " + e.toString());
        }
    }
}
