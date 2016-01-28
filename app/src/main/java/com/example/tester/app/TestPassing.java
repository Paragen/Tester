package com.example.tester.app;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.*;
import com.example.tester.app.vk_login.LoginActivity;
import com.example.tester.app.vk_login.OAuth;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

public class TestPassing extends Activity implements View.OnClickListener {
    private int curr = -1, total, buttonCount = 0, points = 0,id;
    private String[][] questions;
    private RadioGroup radioGroup;
    private TextView question, title;
    private int[] count, answerNum;
    private Button vkButton;
    private ViewSwitcher switcher;
    private  String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        parse(getIntent().getStringExtra("text"));
        name = getIntent().getStringExtra("name");
        id = getIntent().getIntExtra("id",0);
        nextQuestion();
    }

    void init() {
        setContentView(R.layout.activity_test);
        switcher = (ViewSwitcher)findViewById(R.id.passing_switcher);
        radioGroup = (RadioGroup) findViewById(R.id.radio);
        findViewById(R.id.passing_next).setOnClickListener(this);
        question = (TextView) findViewById(R.id.passing_question);
        title = (TextView) findViewById(R.id.passing_title);
        findViewById(R.id.result_finish).setOnClickListener(this);
        findViewById(R.id.result_try).setOnClickListener(this);
        vkButton = (Button)findViewById(R.id.result_vk);
        vkButton.setOnClickListener(this);
        if (OAuth.token == null) {
            vkButton.setEnabled(false);
        }

    }

    private void parse(String s) {
        int a, b;
        String buf;
        a = s.indexOf("|");
        buf = (s.substring(0, a));
        total = Integer.valueOf(buf);
        count = new int[total];
        answerNum = new int[total];
        questions = new String[total][];
        for (int i = 0; i < total; i++) {
            b = s.indexOf("|", a + 1);
            buf = s.substring(a + 1, b);
            count[i] = Integer.valueOf(buf);
            a = b;
            questions[i] = new String[count[i] + 1];
            for (int j = 0; j <= count[i]; j++) {
                b = s.indexOf("|", a + 1);
                buf = s.substring(a + 1, b);
                questions[i][j] = buf;
                a = b;
            }
            b = s.indexOf("|", a + 1);
            buf = s.substring(a + 1, b);
            answerNum[i] = Integer.valueOf(buf);
            a = b;
        }

    }


    void nextQuestion() {
        curr++;
        if (total <= curr) {
            endTest();
            return;
        }
        radioGroup.clearCheck();
        if (count[curr] >= buttonCount) {
            RadioButton rb;
            for (int i = 0; i < count[curr] - buttonCount; ++i) {
                rb = new RadioButton(this);
                rb.setText(questions[curr][i]);
                rb.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                radioGroup.addView(rb);
            }
        } else {
            radioGroup.removeViews(count[curr], buttonCount - count[curr]);
        }
        buttonCount = count[curr];
        question.setText(questions[curr][0]);
        title.setText("Question # " + (curr + 1) + " from " + total);
        for (int i = 0; i < count[curr]; ++i) {
            ((RadioButton) radioGroup.getChildAt(i)).setText(questions[curr][i + 1]);
        }

    }

    void endTest() {
        switcher.showNext();
        if (OAuth.user_id != null) {
            new AsyncLoader(id,points,this,total,name).execute();
        }
        ((TextView)findViewById(R.id.result_result)).setText("Your result : " +points+" correct, " + (total - points) + " incorrect.(" + (double)points/total+")");

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.passing_next:
                int buf = radioGroup.getCheckedRadioButtonId();
                buf = radioGroup.indexOfChild(radioGroup.findViewById(buf));
                if(buf + 1 == answerNum[curr]) {
                    points++;
                }
                nextQuestion();
                break;
            case R.id.result_try:
                curr = -1;
                points = 0;
                switcher.showPrevious();
                nextQuestion();
                break;
            case R.id.result_finish:
                finish();
                break;
            case R.id.result_vk:
                LoginActivity.publish("My result in test \"" + name + "\" :" + (100 *points / total) + "%. Do you can to do it better ?");
                vkButton.setText("OK!");
                vkButton.setClickable(false);
                break;
        }
    }
    static private class AsyncLoader extends AsyncTask<Void,Void,Void> {
        Context context;
        int id, points, count;
        String name;
        AsyncLoader(int id, int points, Context context, int count, String name) {
            super();
            this.id = id;
            this.points = points;
            this.context = context;
            this.count = count;
            this.name = name;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Uri uri = Uri.parse(context.getString(R.string.host_url)).buildUpon().
                        appendQueryParameter("password", "1334rk").appendQueryParameter("command", "save").appendQueryParameter("idtest", Integer.toString(id))
                        .appendQueryParameter("idvk", OAuth.user_id).appendQueryParameter("result", Integer.toString(points)).appendQueryParameter("name", URLEncoder.encode(name, "UTF-8")).
                                appendQueryParameter("count", Integer.toString(count)).build();
                new URL(uri.toString()).openConnection().getInputStream().close();
            } catch (Exception e) {
                Log.d("TestPassing",e.toString());
            }
            return null;
        }
    }
}

