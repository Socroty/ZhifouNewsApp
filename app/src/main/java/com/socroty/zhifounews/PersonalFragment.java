package com.socroty.zhifounews;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class PersonalFragment extends Fragment {

    private String user_name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.personal_fragment, container, false);
        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String user_random_verify_result = "no.return";

        //发送登录数据到服务端，并接收返回值
        SocketClient socketClient = new SocketClient();
        SharedPreferences pref = Objects.requireNonNull(getContext()).getSharedPreferences("user_info", MODE_PRIVATE);
        boolean user_data = pref.getBoolean("user_permit", false);

        if (user_data) {
            try {
                user_name = pref.getString("user_name", "");
                String user_random = pref.getString("user_random", "");
                String data = "user_random_verify" + "/#/" + user_name + "/&/" + user_random;
                user_random_verify_result = socketClient.getDataInfo(data);
            } catch (IOException e) {
                e.printStackTrace();
            }

            switch (user_random_verify_result) {
                case "no.return":
                    Toast.makeText(getContext(), "无法连接到服务器！", Toast.LENGTH_SHORT).show();
                    break;
                case "verify.true":
                    Toast.makeText(getContext(), "欢迎回来：" + user_name, Toast.LENGTH_SHORT).show();
                    break;
                case "verify.false":
                    Toast.makeText(getContext(), "账号离线，请重新登录！", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = getContext().getSharedPreferences("user_info", MODE_PRIVATE).edit();
                    editor.putBoolean("user_permit", false);
                    editor.putString("user_name", "null");
                    editor.putString("user_password", "null");
                    editor.putString("user_random", "null");
                    editor.apply();
                    break;
                case "server.error":
                    Toast.makeText(getContext(), "服务端发生错误，请稍后重试！", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(getContext(), "发生未知错误！", Toast.LENGTH_SHORT).show();
                    break;
            }
        } else {
            //Toast.makeText(getContext(), "无账号在线，请登录！", Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = getContext().getSharedPreferences("user_info", MODE_PRIVATE).edit();
            editor.putBoolean("user_permit", false);
            editor.putString("user_name", "null");
            editor.putString("user_password", "null");
            editor.putString("user_random", "null");
            editor.apply();
        }

        CardView personal_head_card = Objects.requireNonNull(getView()).findViewById(R.id.personal_head_card);
        personal_head_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = Objects.requireNonNull(getContext()).getSharedPreferences("user_info", MODE_PRIVATE);
                boolean user_data = pref.getBoolean("user_permit", false);
                if (user_data) {
                    Intent intent = new Intent(getContext(), PersonalInfoActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getContext(), SignInActivity.class);
                    startActivity(intent);
                }
            }
        });

        CardView personal_cardView_like = getView().findViewById(R.id.personal_like_card);
        CardView personal_cardView_hate = getView().findViewById(R.id.personal_hate_card);

        personal_cardView_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref_like = Objects.requireNonNull(getContext()).getSharedPreferences("user_info", MODE_PRIVATE);
                final boolean user_data_like = pref_like.getBoolean("user_permit", false);
                if (user_data_like) {
                    Intent intent = new Intent(getContext(), NewsTitleListActivity.class);
                    intent.putExtra("Genre", "Favorite");
                    Objects.requireNonNull(getContext()).startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "请登陆后使用收藏夹！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        personal_cardView_hate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PlaceHolderActivity.class);
                intent.putExtra("extra_type", "wechat");
                intent.putExtra("extra_data", "请扫码关注微信公众号！");
                Objects.requireNonNull(getContext()).startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        final CustomPaintDrawable customPaintDrawable = new CustomPaintDrawable();

        CardView personal_head_card = Objects.requireNonNull(getView()).findViewById(R.id.personal_head_card);
        //CardView personal_cardView_last = getView().findViewById(R.id.personal_last_card);
        CardView personal_cardView_like = getView().findViewById(R.id.personal_like_card);
        CardView personal_cardView_hate = getView().findViewById(R.id.personal_hate_card);
        TextView personal_textView_like_1 = getView().findViewById(R.id.personal_like_card_text_1);
        TextView personal_textView_like_2 = getView().findViewById(R.id.personal_like_card_text_2);
        TextView personal_textView_hate_1 = getView().findViewById(R.id.personal_hate_card_text_1);
        TextView personal_textView_hate_2 = getView().findViewById(R.id.personal_hate_card_text_2);
        TextView personal_head_card_text_1 = getView().findViewById(R.id.personal_head_card_text_1);
        TextView personal_head_card_text_2 = getView().findViewById(R.id.personal_head_card_text_2);
        TextView personal_head_card_text_3 = getView().findViewById(R.id.personal_head_card_text_3);
        TextView personal_like_card_text_2 = getView().findViewById(R.id.personal_like_card_text_2);
        TextView personal_hate_card_text_2 = getView().findViewById(R.id.personal_hate_card_text_2);
        View personal_head_card_line = getView().findViewById(R.id.personal_head_card_line);
        TextView personal_head_card_name = getView().findViewById(R.id.personal_head_card_name);
        /*
        Bitmap bitmap = BitmapFactory.decodeResource(view.getContext().getResources(), R.drawable.head_default);
        Palette palette = Palette.from(bitmap).generate();
        int rgb_backgroundColor = palette.getLightVibrantColor(Color.BLACK);
        int rgb_textColor = palette.getLightVibrantColor(Color.WHITE);
        TextView textView_1 = getView().findViewById(R.id.text_personal_1);
        TextView textView_2 = getView().findViewById(R.id.text_personal_2);
        TextView textView_3 = getView().findViewById(R.id.text_personal_3);
        textView_1.setTextColor(rgb_backgroundColor);
        textView_2.setTextColor(rgb_backgroundColor);
        textView_3.setTextColor(rgb_backgroundColor);
        cardView.setCardBackgroundColor(rgb_backgroundColor);
        */

        personal_head_card.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.shape_personal_head));
        //personal_cardView_last.setBackground(getResources().getDrawable(R.drawable.shape_last));
        personal_cardView_like.setBackground(customPaintDrawable.paintDrawable(48, "#40C2AA87"));
        personal_cardView_hate.setBackground(customPaintDrawable.paintDrawable(48, "#200ed145"));
        personal_textView_like_1.setBackground(customPaintDrawable.paintDrawable(12, "#A76914"));
        personal_textView_like_2.setBackground(customPaintDrawable.paintDrawable(12, "#A76914"));
        personal_textView_hate_1.setBackground(customPaintDrawable.paintDrawable(12, "#00c437"));
        personal_textView_hate_2.setBackground(customPaintDrawable.paintDrawable(12, "#00c437"));
        personal_head_card_text_1.setBackground(customPaintDrawable.paintDrawable(12, "#333333"));
        personal_head_card_text_2.setBackground(customPaintDrawable.paintDrawable(12, "#333333"));
        personal_head_card_text_3.setBackground(customPaintDrawable.paintDrawable(12, "#333333"));
        personal_head_card_line.setBackground(customPaintDrawable.paintDrawable(12, "#333333"));
        String profile_data = null;

        SharedPreferences pref = Objects.requireNonNull(getContext()).getSharedPreferences("user_info", MODE_PRIVATE);
        boolean user_data = pref.getBoolean("user_permit", false);
        String user_name = pref.getString("user_name", "");
        String user_id = pref.getString("user_id", "");

        if (user_data) {
            personal_head_card_name.setText(user_name);

            SocketClient socketClient = new SocketClient();
            try {
                profile_data = socketClient.getDataInfo("fetch_user_profile/#/" + user_id);
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert profile_data != null;
            String[] user_profile_info = profile_data.split("/%/");
            personal_head_card_text_1.setText(user_profile_info[0]);
            personal_head_card_text_1.setPadding(12, 0, 12, 0);
            personal_head_card_text_2.setText(user_profile_info[1]);
            personal_head_card_text_2.setPadding(12, 0, 12, 0);
            personal_head_card_text_3.setText(user_profile_info[2]);
            personal_head_card_text_3.setPadding(12, 0, 12, 0);
            personal_like_card_text_2.setText("已同步");
            personal_hate_card_text_2.setText("小程序");
        } else {
            personal_head_card_name.setText("未登录");
            personal_head_card_text_1.setText("偏好在登录后生成");
            personal_head_card_text_1.setPadding(12, 0, 12, 0);
            personal_like_card_text_2.setText("未同步");
            personal_hate_card_text_2.setText("小程序");
        }
    }
}
