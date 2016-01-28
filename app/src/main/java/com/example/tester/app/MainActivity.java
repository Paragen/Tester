package com.example.tester.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import com.example.tester.app.vk_login.LoginActivity;
import com.example.tester.app.vk_login.OAuth;

public class MainActivity extends Activity implements View.OnClickListener {

    public  TextView textView;
    ViewSwitcher  vkSwitcher;
    Button butt3;

    final String LOG_TAG = "Main Activity";
    final  int VK_LOGIN = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewInit();
    }

    private void viewInit() {
        findViewById(R.id.main_button1).setOnClickListener(this);
        findViewById(R.id.main_button2).setOnClickListener(this);
        butt3 = (Button) findViewById(R.id.main_button3);
        butt3.setOnClickListener(this);
        butt3.setEnabled(false);
        findViewById(R.id.main_button_login).setOnClickListener(this);
        findViewById(R.id.main_button_logout).setOnClickListener(this);
        textView = (TextView) findViewById(R.id.greeting_text_view);
        vkSwitcher = (ViewSwitcher) findViewById(R.id.vk_switcher);


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
                if (OAuth.token != null) {
                    startActivity(new Intent(this, ResultActivity.class));
                }
                break;
            case R.id.main_button_login:
                startActivityForResult(new Intent(this, LoginActivity.class).putExtra("login",true),VK_LOGIN);
                butt3.setEnabled(true);
                break;
            case R.id.main_button_logout:
                startActivityForResult(new Intent(this, LoginActivity.class).putExtra("login",false),VK_LOGIN);
                butt3.setEnabled(false);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (OAuth.token != null) {
                LoginActivity.getCurrentName(this);
            }
            vkSwitcher.showNext();
        }
    }


}
