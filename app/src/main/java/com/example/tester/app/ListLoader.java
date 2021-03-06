package com.example.tester.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.*;


import java.io.*;
import java.net.*;


class ListLoader extends AsyncTask<Void, Integer, TestInfo[]> {

    private PublicTestsActivity currentActivity;
    private final String LOG_TAG = "List Loader";

    ListLoader(PublicTestsActivity currentActivity) {
        this.currentActivity = currentActivity;

    }

    void activityUpdate(PublicTestsActivity newActivity) {
        currentActivity = newActivity;
    }

    protected TestInfo[] doInBackground(Void... ignore) {
        HttpURLConnection url = null;
        TestInfo[] tests = null;
        try {
            Uri uri = Uri.parse(currentActivity.getString(R.string.host_url)).buildUpon().appendQueryParameter("password","1334rk").appendQueryParameter("command", "get_list").build();
            url = (HttpURLConnection) new URL(uri.toString()).openConnection();
            InputStream in = url.getInputStream();
            JsonReader reader = new JsonReader(new InputStreamReader(in));
            reader.beginObject();
            reader.nextName();
            int size = reader.nextInt();
            tests = new TestInfo[size];
            reader.nextName();
            reader.beginArray();
            for (int i = 0; i < size; i++) {
                reader.beginObject();
                tests[i] = new TestInfo();
                reader.nextName();
                tests[i].id = reader.nextInt();
                reader.nextName();
                tests[i].name = reader.nextString();
                reader.nextName();
                tests[i].rait = reader.nextInt();
                reader.nextName();
                tests[i].category = reader.nextString();


                reader.endObject();
            }
            reader.endArray();
            reader.endObject();
            reader.close();
        } catch (Exception e) {
            Log.d(LOG_TAG,"loading failed" + e.toString());
        } finally {
            if (url != null) {
                url.disconnect();
            }
        }
        Log.d(LOG_TAG,"Loading - OK");
        return tests;
    }


    @Override
    protected void onPostExecute(TestInfo[] tests) {
        if (tests == null) {
            currentActivity.finish();
        } else {
            currentActivity.tests = tests;
            currentActivity.sort();
            currentActivity.switcher.showNext();
        }
        Log.d(LOG_TAG,"ASYNC TASK _ EXIT");
    }
}