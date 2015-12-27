package com.example.tester.app;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
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
        //Список элементов в ListView
        final String[] elements = new String[] {"1", "2", "3"/*...*/ };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,	android.R.layout.simple_list_item_1, elements);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {

            }
        });


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
