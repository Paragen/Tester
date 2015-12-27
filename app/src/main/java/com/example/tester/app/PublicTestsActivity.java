package com.example.tester.app;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ViewSwitcher;

public class PublicTestsActivity extends Activity{

    ListView listView;
    TestInfo tests[];
    ViewSwitcher switcher;
    ProgressBar progressBar;
    ListLoader loader;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_tests);
        switcher = (ViewSwitcher) findViewById(R.id.switcher);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        listView = (ListView) findViewById(R.id.listView);
        progressBar.setMax(100);
        progressBar.setVisibility(View.VISIBLE);
        loader = new ListLoader(this);
        loader.execute();
    }
    @Override protected void onDestroy() {
        super.onDestroy();
    }

    String[] getString() {
        String str[] = new String[tests.length];
        for (int i = 0; i < str.length; i++) {
            str[i] = tests[i].name + " with rating : " + tests[i].rait;
        }
        return str;
    }
}
