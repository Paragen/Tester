package com.example.tester.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.tester.app.vk_login.LoginActivity;
import com.example.tester.app.vk_login.OAuth;

public class MainActivity extends Activity implements View.OnClickListener {

    Button butt ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.main_button1).setOnClickListener(this);
        findViewById(R.id.main_button2).setOnClickListener(this);
        findViewById(R.id.main_button3).setOnClickListener(this);
        findViewById(R.id.main_button_login).setOnClickListener(this);
        butt = (Button) findViewById(R.id.main_button_post);
        butt.setOnClickListener(this);
        butt.setVisibility(View.INVISIBLE);
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
            case R.id.main_button_login:
                startActivity(new Intent(this, LoginActivity.class));
                if (OAuth.token != null) {
                    butt.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.main_button_post:
                OAuth.publish("Just test it!!!");
                butt.setText("Success!");
                break;
        }
    }


    protected void onDestroy() {
        super.onDestroy();
    }
}
