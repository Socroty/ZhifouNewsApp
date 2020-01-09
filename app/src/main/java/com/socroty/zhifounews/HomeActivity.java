package com.socroty.zhifounews;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        //设置白底黑字状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        //添加Toolbar
        Toolbar toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        //设置TabLayout和ViewPager监听
        TabLayout mTabLayout = findViewById(R.id.home_toolbar_tablayout);
        mTabLayout.setEnabled(false);
        ViewPager mViewPager = findViewById(R.id.home_fragment);
        HomeFragmentAdapter homeFragmentAdapter = new HomeFragmentAdapter(getSupportFragmentManager());

        //绑定TabLayout和ViewPager
        mViewPager.setAdapter(homeFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(3);

        //指定Tab的位置
        TabLayout.Tab headline = mTabLayout.getTabAt(0);
        TabLayout.Tab variety = mTabLayout.getTabAt(1);
        TabLayout.Tab personal = mTabLayout.getTabAt(2);

        //设置Tab标题
        Objects.requireNonNull(headline).setText("头条精选");
        Objects.requireNonNull(variety).setText("分类推荐");
        Objects.requireNonNull(personal).setText("个人中心");

    }

    //添加Toolbar右上角布局
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_home, menu);
        return true;
    }

    //Toolbar右上角按钮点击事件
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home_info) {
            //展示AlertDialog弹窗——关于
            AlertDialog.Builder dialog = new AlertDialog.Builder(HomeActivity.this);
            View view = View.inflate(HomeActivity.this, R.layout.home_info_dialog, null);
            dialog.setTitle("关于  知否新闻");
            dialog.setView(view);
            dialog.setCancelable(false);
            dialog.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel mChannel = new NotificationChannel("my_notification", "推送通知", NotificationManager.IMPORTANCE_LOW);
                        notificationManager.createNotificationChannel(mChannel);
                        Notification notification = new Notification.Builder(HomeActivity.this)
                                .setChannelId("my_notification")
                                .setContentTitle("知否新闻的通知")
                                .setContentText("通知文本通知文本通知文本")
                                .setColor(Color.parseColor("#FFBA00"))
                                .setColorized(true)
                                //.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_notification_large))
                                .setSmallIcon(R.drawable.ic_notification).build();
                        notificationManager.notify(1,notification);
                    }
                }
            });
            dialog.setNegativeButton("反馈", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(HomeActivity.this, FeedbackActivity.class);
                    startActivity(intent);
                }
            });
            dialog.setNeutralButton("查看源代码", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://github.com/socroty"));
                    startActivity(intent);
                }
            });
            dialog.show();
        }
        return true;
    }
}
