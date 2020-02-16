package com.socroty.zhifounews;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

//新闻详情界面
public class NewsParticularActivity extends AppCompatActivity {

    private View contentViewGroup;
    private int followControl;
    private int favoriteControl;
    private String data_follow;
    private String data_favorite;
    private String data_author;
    private String user_id;
    private boolean user_permit;
    private String data_category;
    private String data_id;
    private String data_url;
    private String data_title;
    private String data_image_url;
    private static String data_content;
    private static String data_comment;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_particular_activity);

        //设置透明状态栏
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        if (contentViewGroup == null) {
            contentViewGroup = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        }
        contentViewGroup.setFitsSystemWindows(false);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //允许主线程网络请求
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //接受Intent数据（从新闻列表适配器传输的新闻内容数据）
        final Intent intent = getIntent();
        String extra_data = intent.getStringExtra("extra_id");

        //从文件中取出用户数据
        SharedPreferences pref = getSharedPreferences("user_info", MODE_PRIVATE);
        user_permit = pref.getBoolean("user_permit", false);
        user_id = pref.getString("user_id", "null");

        //解析新闻内容数据
        String[] news_data = Objects.requireNonNull(extra_data).split("/%/");
        data_id = news_data[0];
        data_title = news_data[1];
        data_content = news_data[2];
        data_author = news_data[3];
        data_url = news_data[4];
        data_category = news_data[5];
        data_image_url = news_data[6];
        String data_create_time = news_data[7];
        data_follow = news_data[8];
        data_favorite = news_data[9];

        //解析并展示新闻时间
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateTime = null;
        try {
            dateTime = dateFormat.parse(data_create_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        assert dateTime != null;
        cal.setTime(dateTime);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        String month_result = String.valueOf(month);
        if (month < 10) {
            month_result = "0" + month;
        }
        String day_result = String.valueOf(day);
        if (day < 10) {
            day_result = "0" + day;
        }
        String hour_result = String.valueOf(hour);
        if (hour < 10) {
            hour_result = "0" + hour;
        }
        String minute_result = String.valueOf(minute);
        if (minute < 10) {
            minute_result = "0" + minute;
        }
        TextView news_particular_time_test = findViewById(R.id.news_particular_time_test);
        news_particular_time_test.setText(" " + year + "年 " + month_result + "月" + day_result + "日 " + hour_result + "时" + minute_result + "分 ");
        CustomPaintDrawable customPaintDrawable = new CustomPaintDrawable();
        news_particular_time_test.setBackground(customPaintDrawable.paintDrawable(12, "#333333"));

        //展示新闻标题
        TextView textView_title = findViewById(R.id.news_particular_title);
        textView_title.setText(data_title);
        TextPaint tp = textView_title.getPaint();
        tp.setFakeBoldText(true);

        //新闻内容列表
        final RecyclerView recyclerView = findViewById(R.id.news_particular_context_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ParticularContentListAdapter particularContentListAdapter = new ParticularContentListAdapter(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setAdapter(particularContentListAdapter);
        recyclerView.setFocusable(false);

        //展示新闻作者关注状态
        final TextView news_particular_author_star_text = findViewById(R.id.news_particular_author_star_text);
        final ImageView news_particular_author_star_image = findViewById(R.id.news_particular_author_star_image);
        if (data_follow.equals("exist")) {
            news_particular_author_star_text.setText("已关注   ");
            news_particular_author_star_image.setImageResource(R.drawable.news_author_star_ed);
            followControl = 1;
        } else {
            news_particular_author_star_text.setText("  关注     ");
            news_particular_author_star_image.setImageResource(R.drawable.news_author_star);
            followControl = 0;
        }

        //展示新闻点赞状态
        CardView cardView_news_particular_content_like_card = findViewById(R.id.news_particular_content_like_card);
        CardView cardView_news_particular_content_read_card = findViewById(R.id.news_particular_content_read_card);
        final TextView particular_like_text = findViewById(R.id.particular_like_text);
        if (data_favorite.equals("exist")) {
            particular_like_text.setText("已赞");
            favoriteControl = 1;
        } else {
            particular_like_text.setText("点赞");
            favoriteControl = 0;
        }
        cardView_news_particular_content_like_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_permit) {
                    if (favoriteControl == 1) {
                        particular_like_text.setText("点赞");
                        favoriteControl = 0;
                    } else {
                        particular_like_text.setText("已赞");
                        favoriteControl = 1;
                    }
                } else {
                    FrameLayout frameLayout = findViewById(R.id.news_particular_framelayout);
                    MySnackbar.Custom(frameLayout,"请登录后进行此操作!",1000*3)
                            .backgroundColor(0XFF333333)
                            .radius(24)
                            .margins(16,16,16,16)
                            .show();
                }
            }
        });
        cardView_news_particular_content_read_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(data_url));
                startActivity(intent);
            }
        });

        //展示新闻作者信息
        TextView news_particular_author = findViewById(R.id.news_particular_author);
        news_particular_author.setText(data_author);
        ImageView news_particular_author_image = findViewById(R.id.news_particular_author_image);
        switch (data_author) {
            case "人民日报":
                news_particular_author_image.setImageResource(R.drawable.renminribao);
                break;
            case "Zaker新闻":
                news_particular_author_image.setImageResource(R.drawable.zakerxinwen);
                break;
            case "一点资讯":
                news_particular_author_image.setImageResource(R.drawable.yidianzixun);
                break;
            case "知否新闻":
                news_particular_author_image.setImageResource(R.drawable.zhifouxinwen);
                break;
            case "环球网":
                news_particular_author_image.setImageResource(R.drawable.huanqiuwang);
                break;
            case "第一财经":
                news_particular_author_image.setImageResource(R.drawable.diyicaijing);
                break;
            case "驱动之家":
                news_particular_author_image.setImageResource(R.drawable.qudongzhijia);
                break;
            case "IT之家":
                news_particular_author_image.setImageResource(R.drawable.itzhijia);
                break;
            case "澎湃新闻":
                news_particular_author_image.setImageResource(R.drawable.pengpaixinwen);
                break;
            case "网易新闻":
                news_particular_author_image.setImageResource(R.drawable.wangyixinwen);
                break;
            case "腾讯新闻":
                news_particular_author_image.setImageResource(R.drawable.tengxunxinwen);
                break;
            case "天天快报":
                news_particular_author_image.setImageResource(R.drawable.tiantiankuaibao);
                break;
            case "极客公园":
                news_particular_author_image.setImageResource(R.drawable.jikegongyuan);
                break;
            default:
                news_particular_author_image.setImageResource(R.drawable.head_default);
                break;
        }
        MyNetImageView news_particular_image = findViewById(R.id.news_particular_image_bg);
        news_particular_image.setImageURL(data_image_url);
        ImageView news_particular_author_background = findViewById(R.id.news_particular_author_background);
        switch (data_category) {
            case "时事":
                news_particular_author_background.setImageResource(R.drawable.variety_domestic_bg);
                break;
            case "国际":
                news_particular_author_background.setImageResource(R.drawable.variety_international_bg);
                break;
            case "社会":
                news_particular_author_background.setImageResource(R.drawable.variety_society_bg);
                break;
            case "体育":
                news_particular_author_background.setImageResource(R.drawable.variety_sports_bg);
                break;
            case "娱乐":
                news_particular_author_background.setImageResource(R.drawable.variety_entertainment_bg);
                break;
            case "军事":
                news_particular_author_background.setImageResource(R.drawable.variety_military_bg);
                break;
            case "财经":
                news_particular_author_background.setImageResource(R.drawable.variety_economic_bg);
                break;
            case "时尚":
                news_particular_author_background.setImageResource(R.drawable.variety_fashion_bg);
                break;
            case "科技":
                news_particular_author_background.setImageResource(R.drawable.variety_science_bg);
                break;
            case "官方":
                news_particular_author_background.setImageResource(R.drawable.variety_official_bg);
                break;
            default:
                news_particular_author_background.setImageResource(R.drawable.variety_headline_bg);
                break;
        }
        CardView cardView_author_star = findViewById(R.id.news_particular_author_star);
        cardView_author_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //news_particular_author_star_image.setImageResource(R.drawable.news_author_star_ed);
                if (user_permit) {
                    if (followControl == 1) {
                        news_particular_author_star_text.setText("  关注     ");
                        news_particular_author_star_image.setImageResource(R.drawable.news_author_star);
                        followControl = 0;
                    } else {
                        news_particular_author_star_text.setText("已关注   ");
                        news_particular_author_star_image.setImageResource(R.drawable.news_author_star_ed);
                        followControl = 1;
                    }
                } else {
                    final FrameLayout frameLayout = findViewById(R.id.news_particular_framelayout);
                    MySnackbar.Custom(frameLayout,"请登录后进行此操作!",1000*3)
                            .backgroundColor(0XFF333333)
                            .radius(24)
                            .margins(16,16,16,16)
                            .show();
                }
            }
        });

        //获取新闻评论
        final SocketClient socketClient = new SocketClient();
        try {
            data_comment = socketClient.getDataInfo("data_comment_get/#/" + data_id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CardView particular_comment_ok = findViewById(R.id.particular_comment_ok);
        particular_comment_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FrameLayout frameLayout = findViewById(R.id.news_particular_framelayout);
                MySnackbar.Custom(frameLayout,"评论功能暂不可用！",1000*3)
                        .backgroundColor(0XFF333333)
                        .radius(24)
                        .margins(16,16,16,16)
                        .show();
                /*
                if (user_permit) {
                    final EditText particular_comment_edit = findViewById(R.id.particular_comment_edit);
                    final String comment_text = particular_comment_edit.getText().toString().replace("\n", "/NN/");
                    String result_input_comment = null;
                    if (comment_text.equals("")) {
                        MySnackbar.Custom(frameLayout,"评论内容不可为空!",1000*3)
                                .backgroundColor(0XFF333333)
                                .radius(24)
                                .margins(16,16,16,16)
                                .show();
                    } else {
                        try {
                            result_input_comment = socketClient.getDataInfo("data_comment_insert/#/" + user_id + "/&/" + data_id + "/&/" + comment_text);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String[] result_insert = Objects.requireNonNull(result_input_comment).split("/@/");
                        if (result_insert[0].equals("insert.true")) {
                            MySnackbar.Custom(frameLayout,"评论添加成功!",1000*3)
                                    .backgroundColor(0XFF333333)
                                    .radius(24)
                                    .margins(16,16,16,16)
                                    .show();
                            particular_comment_edit.setText("");
                            try {
                                data_comment = socketClient.getDataInfo("data_comment_get/#/" + data_id);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            particularCommentListAdapter.notifyDataSetChanged();
                        } else if (result_insert[0].equals("exist")) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(NewsParticularActivity.this);
                            dialog.setTitle("您此前已对本文发表评论：");
                            dialog.setMessage(result_insert[1]);
                            dialog.setCancelable(false);
                            dialog.setPositiveButton("放弃", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            dialog.setNegativeButton("替换原评论", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String result_delete = null;
                                    try {
                                        result_delete = socketClient.getDataInfo("data_comment_delete/#/" + user_id + "/&/" + data_id);
                                        socketClient.getDataInfo("data_comment_insert/#/" + user_id + "/&/" + data_id + "/&/" + comment_text);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    assert result_delete != null;
                                    if (result_delete.equals("delete.true")) {
                                        MySnackbar.Custom(frameLayout,"评论修改成功!",1000*3)
                                                .backgroundColor(0XFF333333)
                                                .radius(24)
                                                .margins(16,16,16,16)
                                                .show();
                                        particular_comment_edit.setText("");
                                    } else {
                                        MySnackbar.Custom(frameLayout,"发生未知错误!",1000*3)
                                                .backgroundColor(0XFF333333)
                                                .radius(24)
                                                .margins(16,16,16,16)
                                                .show();
                                    }
                                    try {
                                        data_comment = socketClient.getDataInfo("data_comment_get/#/" + data_id);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    particularCommentListAdapter.notifyDataSetChanged();
                                }
                            });
                            dialog.show();
                        } else {
                            MySnackbar.Custom(frameLayout,"发生未知错误!",1000*3)
                                    .backgroundColor(0XFF333333)
                                    .radius(24)
                                    .margins(16,16,16,16)
                                    .show();
                        }
                    }
                } else {
                    MySnackbar.Custom(frameLayout,"请登录后参与评论!",1000*3)
                            .backgroundColor(0XFF333333)
                            .radius(24)
                            .margins(16,16,16,16)
                            .show();
                }

                 */
            }
        });
    }

    //返回用户对新闻的“点赞”和“关注”信息修改
    @Override
    protected void onDestroy() {
        super.onDestroy();
        String follow_control;
        String favorite_control;
        SocketClient socketClient = new SocketClient();
        if (data_follow.equals("null") && followControl == 1) {
            follow_control = "data_follow_insert";
            try {
                socketClient.getDataInfo(follow_control + "/#/" + data_author + "/&/" + user_id);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (data_follow.equals("exist") && followControl == 0) {
            follow_control = "data_follow_delete";
            try {
                socketClient.getDataInfo(follow_control + "/#/" + data_author + "/&/" + user_id);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (data_favorite.equals("null") && favoriteControl == 1) {
            favorite_control = "data_favorite_insert";
            try {
                socketClient.getDataInfo(favorite_control + "/#/" + user_id + "/&/" + data_id + "/&/" + data_category + "/&/" + data_title + "/&/" + data_image_url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (data_favorite.equals("exist") && favoriteControl == 0) {
            favorite_control = "data_favorite_delete";
            try {
                socketClient.getDataInfo(favorite_control + "/#/" + user_id + "/&/" + data_id);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //新闻内容get方法
    String getDataContent() {
        return data_content;
    }

    //新闻评论get方法
    String getDataComment() {
        return data_comment;
    }
}
