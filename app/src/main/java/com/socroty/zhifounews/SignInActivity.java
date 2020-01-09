package com.socroty.zhifounews;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity);

        //允许主线程网络请求
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //设置白底黑字状态栏
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //实例化
        TextInputLayout textInputLayout_name = findViewById(R.id.sign_in_username);
        TextInputLayout textInputLayout_password = findViewById(R.id.sign_in_password);
        final EditText editText_name = textInputLayout_name.getEditText();
        final EditText editText_password = textInputLayout_password.getEditText();
        CardView sign_in_ok = findViewById(R.id.sign_in_ok);
        TextView textView_forget_password = findViewById(R.id.sign_in_forget);
        TextView textView_to_sign = findViewById(R.id.sign_in_to_sign_up);

        //“登录”按钮点击事件
        sign_in_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sign_in_result = "no.return";
                Random ra = new Random();
                int random = ra.nextInt(899999999) + 100000000;
                String user_random = String.valueOf(random);
                final String user_name = Objects.requireNonNull(editText_name).getText().toString();
                final String user_password = Objects.requireNonNull(editText_password).getText().toString();

                //判断输入内容是否符合格式
                if (user_name.length() < 4 || user_name.length() > 8 ) {
                    editText_name.setError("长度必须为4-8位!");
                } else if (user_password.length() < 8 || user_password.length() > 16) {
                    editText_password.setError("长度必须为8-16位!");
                } else {
                    if (inputSpecialChar(user_name)) {
                        editText_name.setError("禁止包含特殊字符！");
                    } else if (inputSpecialChar(user_password)) {
                        editText_password.setError("禁止包含特殊字符！");
                    } else {
                        //发送登录数据到服务端，并接收返回值
                        final String user_data = "user_sign_in" + "/#/" + user_name + "/&/" + user_password + "/&/" + user_random;
                        SocketClient socketClient = new SocketClient();
                        try {
                            sign_in_result = socketClient.getDataInfo(user_data);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //判断返回值，并执行相应操作
                        String[] sign_in_info = sign_in_result.split("/%/");
                        switch (sign_in_info[0]) {
                            case "sign.in.null":
                                Toast.makeText(SignInActivity.this, "此用户名未注册！", Toast.LENGTH_SHORT).show();
                                break;
                            case "sign.in.false":
                                Toast.makeText(SignInActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
                                break;
                            case "sign.in.true":
                                SharedPreferences.Editor editor_user_info = getSharedPreferences("user_info", MODE_PRIVATE).edit();
                                editor_user_info.putBoolean("user_permit", true);
                                editor_user_info.putString("user_id",sign_in_info[1]);
                                editor_user_info.putString("user_name", user_name);
                                editor_user_info.putString("user_password", user_password);
                                editor_user_info.putString("user_random", user_random);
                                editor_user_info.apply();
                                Toast.makeText(SignInActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                                finish();
                                break;
                            case "server.error":
                                Toast.makeText(SignInActivity.this, "服务端发生错误，请稍后重试！", Toast.LENGTH_SHORT).show();
                                break;
                            case "no.return":
                                Toast.makeText(SignInActivity.this, "未接受到服务器返回数据！", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(SignInActivity.this, "发生未知错误！", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }
            }
        });

        //“没有账号？”点击事件
        textView_to_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        //“忘记密码？”点击事件
        textView_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignInActivity.this, "功能未开放！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //特殊字符判断
    private static boolean inputSpecialChar(String str) {
        String speChat = "[`~!@#_$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）— +|{}【】‘；：”“’。，、？]";
        Pattern pattern = Pattern.compile(speChat);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }
}
