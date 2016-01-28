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

public class PublicTestsActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {

    ListView listView;
    TestInfo tests[], currTests[];
    ViewSwitcher switcher;
    ProgressBar progressBar;
    ListLoader loader;
    String[] catList;
    int currSize;
    final String LOG_TAG = "Public Test Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_tests);
        switcher = (ViewSwitcher) findViewById(R.id.switcher);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        ((Button) findViewById(R.id.list_button)).setOnClickListener(this);
        loader = new ListLoader(this);
        try {
            catList = getResources().getStringArray(R.array.category);
        } catch (Exception e) {
            e.printStackTrace();
        }
        loader.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    String[][] getString() {
        String str[][] = new String[2][currSize];
        for (int i = 0; i < currSize; i++) {
            str[0][i] = currTests[i].name;
            str[1][i] = catList[Integer.valueOf(currTests[i].category)];
        }
        return str;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switcher.showPrevious();
        new AsyncLoader(this, currTests[i].id, currTests[i].name).execute();
    }

    @Override
    public void onClick(View view) {
        sort();
    }

    static private class AsyncLoader extends AsyncTask<Void, String, String> {

        PublicTestsActivity context;
        int id;
        String name;

        AsyncLoader(PublicTestsActivity context, int id, String name) {
            this.id = id;
            this.context = context;
            this.name = name;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String answer = null;
            try {
                Uri uri = Uri.parse(context.getString(R.string.host_url)).buildUpon().
                        appendQueryParameter("command", "get_test").appendQueryParameter("password", "1334rk").appendQueryParameter("id", "" + id).build();
                InputStreamReader reader = new InputStreamReader(new URL(uri.toString()).openConnection().getInputStream());
                char buf[] = new char[10000];
                int size = reader.read(buf);
                answer = new String(buf, 0, size);
                Log.d("AsyncLoader", "answer: " + answer);
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return answer;
        }

        @Override
        protected void onPostExecute(String answer) {
            if (answer != null) {
                try {
                    context.startActivity(new Intent(context, TestPassing.class).putExtra("text", answer).putExtra("name", name).putExtra("id", id));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            context.switcher.showNext();
        }
    }

    void sort() {
        String key = ((EditText) findViewById(R.id.list_edit)).getText().toString().toUpperCase();
        String cat = Integer.toString(((Spinner) findViewById(R.id.list_spinner)).getSelectedItemPosition());
        TestInfo[] buf = new TestInfo[tests.length];
        TestInfo curr;
        int size = 0;
        for (int i = 0; i < tests.length; ++i) {
            curr = tests[i];
            if (("0".equals(cat) || curr.category.equals(cat)) && ("".equals(key) || curr.name.toUpperCase().contains(key))) {
                buf[size++] = curr;
            }
        }
        currTests = buf;
        currSize = size;
        listView.setAdapter(new TestsAdapter(this, getString()));
    }
}
