package com.socroty.zhifounews;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);

        //允许主线程网络请求
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //设置白底黑字状态栏
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //实例化
        TextInputLayout textInputLayout_name = findViewById(R.id.sign_up_username);
        TextInputLayout textInputLayout_password = findViewById(R.id.sign_up_password);
        TextInputLayout textInputLayout_password_again = findViewById(R.id.sign_up_password_again);
        final EditText editText_name = textInputLayout_name.getEditText();
        final EditText editText_password = textInputLayout_password.getEditText();
        final EditText editText_password_again = textInputLayout_password_again.getEditText();
        textInputLayout_name.setErrorEnabled(false);
        textInputLayout_password.setErrorEnabled(false);
        textInputLayout_password_again.setErrorEnabled(false);
        CardView sign_up_ok = findViewById(R.id.sign_up_ok);
        TextView textView_to_sign = findViewById(R.id.sign_up_to_sign_in);

        sign_up_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sign_up_result = "no.return";
                final String user_name = Objects.requireNonNull(editText_name).getText().toString();
                final String user_password = Objects.requireNonNull(editText_password).getText().toString();
                final String user_password_again = Objects.requireNonNull(editText_password_again).getText().toString();

                if (user_name.length() < 4 || user_name.length() > 8 ) {
                    editText_name.setError("长度必须为4-8位");
                } else if (user_password.length() < 8 || user_password.length() > 16) {
                    editText_password.setError("长度必须为8-16位");
                } else if (inputSpecialChar(user_name)) {
                    editText_name.setError("禁止包含特殊字符！");
                } else if (inputSpecialChar(user_password)) {
                    editText_password.setError("禁止包含特殊字符！");
                } else {
                    if (user_password.equals(user_password_again)) {
                        final String user_data = "user_sign_up" + "/#/" + user_name + "/&/" + user_password;
                        SocketClient socketClient = new SocketClient();
                        try {
                            sign_up_result = socketClient.getDataInfo(user_data);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        switch (sign_up_result) {
                            case "sign.up.exist":
                                Toast.makeText(SignUpActivity.this, "此用户名已被注册！", Toast.LENGTH_SHORT).show();
                                break;
                            case "sign.up.false":
                                Toast.makeText(SignUpActivity.this, "写入用户信息失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                                break;
                            case "sign.up.true":
                                Toast.makeText(SignUpActivity.this, "注册完成，请登录！", Toast.LENGTH_SHORT).show();
                                finish();
                                break;
                            case "server.error":
                                Toast.makeText(SignUpActivity.this, "服务端发生错误，请稍后重试！", Toast.LENGTH_SHORT).show();
                                break;
                            case "no.return":
                                Toast.makeText(SignUpActivity.this, "未接受到服务器返回数据！", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(SignUpActivity.this, "发生未知错误！", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    } else {
                        Toast.makeText(SignUpActivity.this, "请核对两次输入密码！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //“返回登录？”点击事件
        textView_to_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
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
