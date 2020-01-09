package com.socroty.zhifounews;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;

public class NewsTitleListActivity extends AppCompatActivity {

    private static String title_data;
    private static int title_length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_title_list_activity);

        //允许主线程网络请求
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //设置白底黑字状态栏
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //获取上一个活动传递的数据
        Intent intent = getIntent();
        String intent_data = intent.getStringExtra("Genre");
        final String user_data;
        SharedPreferences pref = NewsTitleListActivity.this.getSharedPreferences("user_info", MODE_PRIVATE);
        String user_id = pref.getString("user_id", "");
        if (intent_data.equals("Favorite")) {
            user_data = "data_favorite_title" + "/#/" + user_id;
        } else if (intent_data.equals("Preference")){
            user_data = "fetch_profile_title" + "/#/" + user_id;
        } else{
            user_data = "data_get_title" + "/#/" + intent_data;
        }

        SocketClient socketClient = new SocketClient();
        try {
            title_data = socketClient.getDataInfo(user_data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (title_data != null) {
            if (!title_data.equals("/@//@/")) {

                String[] data = title_data.split("/@/");
                String[] data_title = data[0].split("/%/");
                title_length = data_title.length;
                final RecyclerView recyclerView = findViewById(R.id.news_list);
                NewsTitleListAdapter newsTitleListAdapterTitle = new NewsTitleListAdapter(this);
                recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                recyclerView.setItemViewCacheSize(20);
                recyclerView.setAdapter(newsTitleListAdapterTitle);

                TextView textView_main = findViewById(R.id.news_list_name);
                TextView textView_sub = findViewById(R.id.news_list_name_2);
                final CustomPaintDrawable customPaintDrawable = new CustomPaintDrawable();
                textView_sub.setBackground(customPaintDrawable.paintDrawable(16, "#333333"));
                textView_sub.setPadding(16, 0, 16, 0);

                switch (intent_data) {
                    case "Preference":
                        textView_main.setText("推荐");
                        textView_sub.setText("Preference");
                        break;
                    case "Domestic":
                        textView_main.setText("国内");
                        textView_sub.setText("Domestic");
                        break;
                    case "International":
                        textView_main.setText("国际");
                        textView_sub.setText("International");
                        break;
                    case "Society":
                        textView_main.setText("社会");
                        textView_sub.setText("Society");
                        break;
                    case "Sports":
                        textView_main.setText("体育");
                        textView_sub.setText("Sports");
                        break;
                    case "Amusement":
                        textView_main.setText("娱乐");
                        textView_sub.setText("Amusement");
                        break;
                    case "Military":
                        textView_main.setText("军事");
                        textView_sub.setText("Military");
                        break;
                    case "Economics":
                        textView_main.setText("财经");
                        textView_sub.setText("Economics");
                        break;
                    case "Fashion":
                        textView_main.setText("时尚");
                        textView_sub.setText("Fashion");
                        break;
                    case "Science":
                        textView_main.setText("科技");
                        textView_sub.setText("Science");
                        break;
                    case "Favorite":
                        textView_main.setText("收藏");
                        textView_sub.setText("Favorite");
                        break;
                }
            }else {
                Intent intent_no_title = new Intent(NewsTitleListActivity.this, PlaceHolderActivity.class);
                intent_no_title.putExtra("extra_type", "blank");
                intent_no_title.putExtra("extra_data", "收藏夹内无内容！");
                startActivity(intent_no_title);
                finish();
            }
        } else {
            Intent intent_error = new Intent();
            intent_error.setClass(NewsTitleListActivity.this, ErrorActivity.class);
            intent_error.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
            intent_error.putExtra("extra_data", "服务离线！请点此返回重试！");
            startActivity(intent_error);
            finish();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        title_data = null;
    }

    String getExtraData() {
        return title_data;
    }

    int getTitleLength() {
        return title_length;
    }
}
