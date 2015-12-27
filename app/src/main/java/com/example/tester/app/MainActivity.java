package com.example.tester.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.main_button1).setOnClickListener(this);
        findViewById(R.id.main_button2).setOnClickListener(this);
        findViewById(R.id.main_button3).setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_button1:
                startActivity(new Intent(this, PublicTestsActivity.class));
                break;
            case R.id.main_button2:
                startActivity(new Intent(this, CreateTestActivity.class));
                break;
            case R.id.main_button3:
                startActivity(new Intent(this, ResultActivity.class));
                break;
        }
    }


    protected void onDestroy() {
        super.onDestroy();
    }
}
