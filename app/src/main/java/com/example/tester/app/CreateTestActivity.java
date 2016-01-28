package com.example.tester.app;

        import android.app.Activity;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.media.Image;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.KeyEvent;
        import android.view.View;
        import android.widget.*;

        import java.net.URL;
        import java.net.URLEncoder;

public class CreateTestActivity extends Activity implements View.OnClickListener , View.OnKeyListener{

    ViewFlipper flipper,flipperMain;
    Button  buttonNext, buttonTest;
    EditText editName, editCount, editText, editAnswerCount;
    LinearLayout scroll;
    TextView titleView;

    String[][] questions = new String[200][100];
    Boolean[] valid = new Boolean[200];
    int [] countM = new int[200], correctM = new int[200];
    int count = 0, curr = 0;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_test);
        init();
    }

    void init() {
       flipper =  (ViewFlipper)findViewById(R.id.create_flipper);
        scroll = (LinearLayout)findViewById(R.id.create_scroll);
        flipperMain = (ViewFlipper) findViewById(R.id.create_flipper_main);
        buttonNext = (Button)findViewById(R.id.create_next);
        buttonTest = (Button) findViewById(R.id.create_test);
        buttonTest.setOnClickListener(this);
        buttonNext.setOnClickListener(this);
        findViewById(R.id.create_info).setOnClickListener(this);
        findViewById(R.id.create_add).setOnClickListener(this);
        findViewById(R.id.create_delete).setOnClickListener(this);
        findViewById(R.id.create_previous).setOnClickListener(this);
        findViewById(R.id.create_back).setOnClickListener(this);
        titleView = (TextView) findViewById(R.id.create_title);
        editCount = (EditText)findViewById(R.id.create_count);
        editName = (EditText)findViewById(R.id.create_name);
        editText = (EditText)findViewById(R.id.create_question);
        editAnswerCount = (EditText) findViewById(R.id.create_count_sub);
        for (int i = 0; i < 200; i++) {
            valid[i] = false;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_info:
                flipper.setDisplayedChild(0);
                break;
            case R.id.create_test:
                name = editName.getText().toString();
                if (editCount.getText().toString().equals("")) {
                    count = 0;
                } else {
                    count = Integer.parseInt(editCount.getText().toString());
                }
                if (count > 0 && !name.equals("")) {
                    curr = (curr < count) ? curr : count;
                    flipper.setDisplayedChild(1);
                    setQuestion(curr);
                }
                break;
            case R.id.create_previous:
                if (curr > 0) {
                    saveCurrent();
                    buttonNext.setText("Next");
                    setQuestion(curr-1);
                }
                break;
            case R.id.create_next:
                saveCurrent();
                ++curr;
                if (curr < count) {
                    setQuestion(curr);
                    if (curr+1==count) {
                        buttonNext.setText("Finish");
                    }
                } else {
                    int var = 0;
                    for (int i = 0; i < count; ++i) {
                        if (valid[i]) {
                            ++var;
                        }
                    }
                    if (var == count) {
                        new AsyncLoader(this, combineTest()).execute();
                        flipperMain.setDisplayedChild(1);
                    }
                    else {
                        --curr;
                    }
                }
                break;
            case R.id.create_back:
                finish();
                break;
            case R.id.create_add:
                scroll.addView(new EditText(this));
                break;
            case R.id.create_delete:
                if (scroll.getChildCount() > 0) {
                    scroll.removeViews(scroll.getChildCount()-1,1);
                }
                break;
        }
    }

    private void setQuestion(int num) {
        curr = num;
        titleView.setText("Question #" + (curr+1) + " from " + count);
        if (valid[curr]) {
            if (scroll.getChildCount() > countM[curr]) {
                scroll.removeViews(countM[curr],scroll.getChildCount() - countM[curr]);
            } else {
                for (int i = 0, border = countM[curr] - scroll.getChildCount(); i < border; ++i) {
                    scroll.addView(new EditText(this));
                }
            }
            for (int i = 0; i < countM[curr]; ++i) {
                ((EditText)scroll.getChildAt(i)).setText(questions[curr][i+1]);
            }
            editText.setText(questions[curr][0]);
            editAnswerCount.setText(Integer.toString(correctM[curr]));
        } else {
            correctM[curr] = 0;
            countM[curr] = 0;
            scroll.removeAllViews();
            editText.setText("");
            editAnswerCount.setText("");
        }
    }

    void saveCurrent() {
        questions[curr][0] = editText.getText().toString();
        countM[curr] = scroll.getChildCount();
        if (editAnswerCount.getText().toString().equals("")) {
            correctM[curr] = 0;
        } else {
            correctM[curr] = Integer.parseInt(editAnswerCount.getText().toString());
        }
        for (int i = 0; i < countM[curr]; ++i) {
            questions[curr][i+1] = ((EditText)scroll.getChildAt(i)).getText().toString();
        }
        if (correctM[curr] >= 0 && correctM[curr] <= countM[curr] && !questions[curr][0].equals("")) {
            valid[curr] = true;
        } else {
            valid[curr] = false;
        }
    }
    private String[] combineTest() {
        String[] buf  = new String[4];
        buf[0] = Integer.toString(((Spinner) findViewById(R.id.create_category)).getSelectedItemPosition());
        buf[1] = name;
        StringBuilder s = new StringBuilder(Integer.toString(count));
        for (int i = 0; i < count; ++i) {
            s.append("|");
            s.append(countM[i]);
            for (int j = 0; j <= countM[i]; ++j) {
                s.append("|");
                s.append(questions[i][j]);
            }
            s.append("|");
            s.append(Integer.toString(correctM[i]));
        }
        s.append("|");
        buf[2] = s.toString();
        buf[3] = "true";
        return  buf;
    }
    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        return false;
    }

    static private class AsyncLoader extends AsyncTask<Void,Boolean,Boolean> {
        String buf[];
        Activity context;
        AsyncLoader(Activity context, String[] buf) {
            this.context = context;
            this.buf = buf;
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Uri uri = Uri.parse(context.getString(R.string.host_url)).buildUpon().appendQueryParameter("command","create_test").appendQueryParameter("category",URLEncoder.encode(buf[0], "UTF-8"))
                        .appendQueryParameter("name",URLEncoder.encode(buf[1], "UTF-8")).appendQueryParameter("test", URLEncoder.encode(buf[2], "UTF-8")).appendQueryParameter("private",buf[3]).appendQueryParameter("password","1334rk").build();
                new URL(uri.toString()).openConnection().getInputStream().close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean param) {
            TextView view = (TextView)context.findViewById(R.id.create_status);
            if (param) {
                view.setText("Success!");
            } else {
                view.setText("Ops, something wrong :(");
            }
        }
    }

}