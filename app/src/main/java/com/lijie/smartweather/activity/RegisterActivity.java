package com.lijie.smartweather.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lijie.smartweather.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RegisterActivity extends AppCompatActivity {

    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.cv_add)
    CardView cvAdd;
    @Bind(R.id.bt_go)
    Button btGo;
    @Bind(R.id.et_username)
    EditText etUsername;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.et_repeatpassword)
    EditText etRepeatpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ShowEnterAnimation();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateRevealClose();
            }
        });
    }

    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                cvAdd.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }


        });
    }

    public void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, fab.getWidth() / 2, cvAdd.getHeight());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                cvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    public void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, cvAdd.getHeight(), fab.getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cvAdd.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                fab.setImageResource(R.drawable.plus);
                RegisterActivity.super.onBackPressed();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    @Override
    public void onBackPressed() {
        animateRevealClose();
    }

    @OnClick({R.id.bt_go})

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_go:
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);

                String checkResult = checkInfo();
                if (checkResult != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            RegisterActivity.this);
                    builder.setTitle("出错提示");
                    builder.setMessage(checkResult);
                    builder.setPositiveButton("正确",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    etPassword.setText("");
                                    etRepeatpassword.setText("");
                                }
                            });
                    builder.create().show();
                } else {
                    final Handler myHandler = new Handler() {
                        public void handleMessage(Message msg) {
                            boolean b = (Boolean) msg.obj;
                            if (b) {
                                Intent intent = new Intent(
                                        RegisterActivity.this,
                                        LoginActivity.class);
                                startActivity(intent);
                                Toast.makeText(RegisterActivity.this, "注册成功",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "注册失败",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    };
                    new Thread() {
                        public void run() {
                            boolean b = false;
                            RegisterToServer registerToServer = new RegisterToServer();
                            String response = registerToServer.doGet(etUsername
                                    .getText().toString(), etPassword.getText()
                                    .toString());
                            if ("true".equals(response)) {
                                b = true;
                            } else {
                                b = false;
                            }
                            Message message = new Message();
                            message.obj = b;
                            myHandler.sendMessage(message);
                        }
                    }.start();
                }
                break;
        }


    }

    public String checkInfo() {
        if (etUsername.getText().toString() == null
                || etUsername.getText().toString().equals("")) {
            return "用户名不能为空";
        }
        if (etPassword.getText().toString().trim().length() < 3
                || etPassword.getText().toString().trim().length() > 15) {
            return "密码只能在3-15个字符之间";
        }
        if (!etRepeatpassword.getText().toString().equals(etPassword.getText().toString())) {
            return "前后两次密码不同，请重新输入";
        }
        return null;
    }
}
