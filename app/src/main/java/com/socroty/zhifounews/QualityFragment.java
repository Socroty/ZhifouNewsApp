package com.socroty.zhifounews;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class QualityFragment extends Fragment {
    String data = null;
    String particular_data = null;
    int pager_id = 0;
    int pager_state = 0;
    private String[] headline_data_id;
    private String[] headline_id;
    private String[] headline_title;
    private Drawable[] head_image;
    private ProgressDialog progressDialog;
    private TextSwitcher textSwitcher = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.quality_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //允许主线程网络请求
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        View line = Objects.requireNonNull(getView()).findViewById(R.id.quality_headline_card_line);
        View line_end = getView().findViewById(R.id.quality_headline_end_line);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(60);
        drawable.setColor(Color.parseColor("#A24902"));
        line.setBackground(drawable);
        line_end.setBackground(drawable);

        final SocketClient socketClient = new SocketClient();
        try {
            data = socketClient.getDataInfo("headline_get_info/#/null");
        } catch (IOException e) {
            e.printStackTrace();
        }

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("请稍后...");
        progressDialog.setMessage("已连接到服务器\n正在同步数据...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        if (data != null) {
            String[] data_headline = Objects.requireNonNull(data).split("/@/");
            headline_data_id = data_headline[0].split("/%/");
            headline_id = data_headline[1].split("/%/");
            headline_title = data_headline[2].split("/%/");
            head_image = new Drawable[6];

            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 6; i++) {
                        File file_local = new File(Objects.requireNonNull(getContext()).getFilesDir(), headline_id[i + 1] + ".png");
                        if (!file_local.exists()) {
                            try {
                                socketClient.getHeadlineImage(headline_id[i + 1], getContext());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            FileInputStream fileInputStream = new FileInputStream(file_local);
                            head_image[i] = new BitmapDrawable(getResources(), BitmapFactory.decodeStream(fileInputStream));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }
            }).start();

        } else {
            Intent intent = new Intent();
            intent.setClass(Objects.requireNonNull(getContext()), ErrorActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
            intent.putExtra("extra_data", "服务离线！请点此关闭程序！");
            startActivity(intent);
            Objects.requireNonNull(getActivity()).finish();
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                //添加Headlines卡片
                QualityHeadViewAdapter qualityHeadViewAdapter = new QualityHeadViewAdapter();
                qualityHeadViewAdapter.addHeadlineItem(new QualityHeadViewItem(head_image[0]));
                qualityHeadViewAdapter.addHeadlineItem(new QualityHeadViewItem(head_image[1]));
                qualityHeadViewAdapter.addHeadlineItem(new QualityHeadViewItem(head_image[2]));
                qualityHeadViewAdapter.addHeadlineItem(new QualityHeadViewItem(head_image[3]));
                qualityHeadViewAdapter.addHeadlineItem(new QualityHeadViewItem(head_image[4]));
                qualityHeadViewAdapter.addHeadlineItem(new QualityHeadViewItem(head_image[5]));

                //卡片效果设置
                ViewPager viewPager = Objects.requireNonNull(getView()).findViewById(R.id.quality_head_view);
                QualityHeadViewTransformer qualityHeadViewTransformer = new QualityHeadViewTransformer(viewPager, qualityHeadViewAdapter);
                viewPager.setAdapter(qualityHeadViewAdapter);
                viewPager.setCurrentItem(0);//定义起始页
                viewPager.setPageTransformer(false, qualityHeadViewTransformer);//设置切换动画
                viewPager.setOffscreenPageLimit(5);
                qualityHeadViewTransformer.enableScaling();

                viewPager.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                            if (pager_state == 0) {
                                view.performClick();
                            }
                        }
                        return false;
                    }
                }
                );

                viewPager.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SocketClient socketClient = new SocketClient();
                        SharedPreferences pref = Objects.requireNonNull(getContext()).getSharedPreferences("user_info", MODE_PRIVATE);
                        final String user_id = pref.getString("user_id", "null");
                        try {
                            particular_data = socketClient.getDataInfo("data_get_info" + "/#/" + headline_data_id[pager_id+1] + "/&/" + user_id);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (particular_data != null) {
                            Intent intent = new Intent(getContext(), NewsParticularActivity.class);
                            intent.putExtra("extra_id", particular_data);
                            getContext().startActivity(intent);
                        } else {
                            Intent intent_error = new Intent();
                            intent_error.setClass(getContext(), ErrorActivity.class);
                            intent_error.putExtra("extra_data", "服务离线！请点此返回重试！");
                            getContext().startActivity(intent_error);
                            Activity activity = (Activity) getContext();
                            activity.finish();
                        }
                    }
                });

                textSwitcher = getView().findViewById(R.id.headline_title_textswitcher);
                textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
                    @Override
                    public View makeView() {
                        TextView tv = new TextView(getContext());
                        tv.setTextSize(21);
                        tv.setTextColor(Color.BLACK);
                        return tv;
                    }
                });
                textSwitcher.setText(headline_title[1]);

                textSwitcher.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SocketClient socketClient = new SocketClient();
                        SharedPreferences pref = Objects.requireNonNull(getContext()).getSharedPreferences("user_info", MODE_PRIVATE);
                        final String user_id = pref.getString("user_id", "null");
                        try {
                            particular_data = socketClient.getDataInfo("data_get_info" + "/#/" + headline_data_id[pager_id+1] + "/&/" + user_id);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (particular_data != null) {
                            Intent intent = new Intent(getContext(), NewsParticularActivity.class);
                            intent.putExtra("extra_id", particular_data);
                            getContext().startActivity(intent);
                        } else {
                            Intent intent_error = new Intent();
                            intent_error.setClass(getContext(), ErrorActivity.class);
                            intent_error.putExtra("extra_data", "服务离线！请点此返回重试！");
                            getContext().startActivity(intent_error);
                            Activity activity = (Activity) getContext();
                            activity.finish();
                        }
                    }
                });

                final TextView textView_num = getView().findViewById(R.id.quality_headline_number);

                //监听View滑动
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int i, float v, int i1) {
                        pager_state = i1;
                    }

                    @Override
                    public void onPageSelected(final int i) {
                        pager_id = i;
                        textSwitcher.setText(headline_title[i+1]);
                        textView_num.setText(i+1+"/6");
                    }

                    @Override
                    public void onPageScrollStateChanged(int i) {

                    }
                });

                TextView quality_blow_text = getView().findViewById(R.id.quality_blow_text);
                quality_blow_text.setText("以下为个性化推荐内容");
                //CardView quality_blow_card = getView().findViewById(R.id.quality_blow_card);
                //quality_blow_card.setCardBackgroundColor(Color.parseColor("#FFF0A2"));

                final RecyclerView recyclerView = getView().findViewById(R.id.quality_news_list);
                QualityNewsTitleListAdapter qualityNewsTitleListAdapter = new QualityNewsTitleListAdapter(getContext());
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                recyclerView.setItemViewCacheSize(20);
                recyclerView.setAdapter(qualityNewsTitleListAdapter);
                progressDialog.dismiss();
            }
        }
    };
}
