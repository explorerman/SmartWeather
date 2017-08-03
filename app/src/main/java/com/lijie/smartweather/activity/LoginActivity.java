package com.lijie.smartweather.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lijie.smartweather.FirstActivity;
import com.lijie.smartweather.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lijie on 17/6/19.
 */

public class LoginActivity extends AppCompatActivity {
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
//    private EditText mPhoneNumberEditText;
//    private EditText mPassWordEditText;
//    private Button mLoginButton;
//    private String originAddress = "http://172.20.10.4:8080/Server/servlet/LoginServlet";
//
//    Handler mHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            String result = "";
//
//            if ("OK".equals(msg.obj.toString())){
//                result = "success";
//                FirstActivity.launch(LoginActivity.this);
//            }else if ("Wrong".equals(msg.obj.toString())){
//                result = "fail";
//            }else {
//                result = msg.obj.toString();
//            }
//            Toast.makeText(LoginActivity.this, result, Toast.LENGTH_LONG).show();
//        }
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.login_activity);
//        initView();
//        initEvent();
//    }
//
//    private void initView() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        mPhoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText);
//        mPassWordEditText = (EditText) findViewById(R.id.passwordEditText);
//        mLoginButton = (Button) findViewById(R.id.loginButton);
//    }
//
//    private void initEvent() {
//        mLoginButton.setOnClickListener(this);
//    }
//
//    public void login() {
//        //取得用户输入的账号和密码
//        if (!isInputValid()){
//            return;
//        }
//        HashMap<String, String> params = new HashMap<String, String>();
//        params.put(User.PHONENUMBER, mPhoneNumberEditText.getText().toString());
//        params.put(User.PASSWORD, mPassWordEditText.getText().toString());
//        try {
//            String compeletedURL = ServletUtil.getURLWithParams(originAddress, params);
//            ServletUtil.sendHttpRequest(compeletedURL, new HttpCallbackListener() {
//                @Override
//                public void onFinish(String response) {
//                    Message message = new Message();
//                    message.obj = response;
//                    mHandler.sendMessage(message);
//                }
//
//                @Override
//                public void onError(Exception e) {
//                    Message message = new Message();
//                    message.obj = e.toString();
//                    mHandler.sendMessage(message);
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private boolean isInputValid() {
//        //检查用户输入的合法性，这里暂且默认用户输入合法
//        return true;
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.loginButton:
////                FirstActivity.launch(LoginActivity.this);
////                finish();
//                login();
//                finish();
//                break;
//        }
//    }

    private EditText name, password;
    private Button login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);

        name = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.psd);
        login = (Button) findViewById(R.id.login);
        setSupportActionBar(toolbar);

        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Explode explode = new Explode();
                explode.setDuration(500);

                getWindow().setExitTransition(explode);
                getWindow().setEnterTransition(explode);

                final Handler myHandler = new Handler() {
                    public void handleMessage(Message msg) {
                        boolean b = (Boolean) msg.obj;
                        if (b) {
                            Intent intent = new Intent(LoginActivity.this, FirstActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("name", name.getText().toString());
                            bundle.putString("psd", password.getText().toString());
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                new Thread() {
                    public void run() {
                        boolean b = checkUser(name.getText().toString(), password.getText().toString());
                        Message message = new Message();
                        message.obj = b;
                        myHandler.sendMessage(message);
                    }
                }.start();
            }
        });
    }

    @OnClick({R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(this, fab, fab.getTransitionName());
                    startActivity(new Intent(this, RegisterActivity.class), options.toBundle());
                } else {
                    startActivity(new Intent(this, RegisterActivity.class));
                }
                break;
        }
    }

    public boolean checkUser(String name, String psd) {
        LoginToServer loginToServer = new LoginToServer();
        String response = loginToServer.doGet(name, psd);
        if ("true".equals(response)) {
            return true;
        } else {
            return false;
        }
    }

}
