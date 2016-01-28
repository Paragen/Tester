package com.example.tester.app;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.io.InputStreamReader;
import java.net.URL;

public class ResultActivity extends Activity implements AdapterView.OnItemClickListener{

    ListView listView;
    TestInfo tests[];
    ResultLoader loader;
    ProgressBar progressBar;
    TextView textView;
    ViewFlipper flipper;
    final String LOG_TAG = "Public Test Activity";
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        listView = (ListView) findViewById(R.id.myresults_listView);
        listView.setOnItemClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.myresults_progress);
        textView = (TextView) findViewById(R.id.myresults_title);
        flipper = (ViewFlipper)findViewById(R.id.myresults_flipper);
        loader = new ResultLoader(this);
        loader.execute();
    }
    @Override protected void onDestroy() {
        super.onDestroy();
    }

    void setList() {
        String str[] = new String[tests.length];
        int[][] res = new int[2][tests.length];
        for (int i = 0; i < str.length; i++) {
            str[i] = tests[i].name ;
            res[0][i] = tests[i].rait;
            res[1][i] = tests[i].id;
        }
        listView.setAdapter(new ResultAdapter(this,str,res));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    }


}
