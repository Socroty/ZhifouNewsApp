package com.socroty.zhifounews;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

public class QualityFragment extends Fragment {
    private String[] headline_title;
    private String[] headline_content;
    private String[] headline_background;
    private String[] headline_id;
    private Drawable[] head_image;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.quality_fragment, container, false);
        return view;
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {

                //添加Headlines卡片
                QualityHeadViewAdapter qualityHeadViewAdapter = new QualityHeadViewAdapter();
                String textColor = "#ffffff";
                qualityHeadViewAdapter.addHeadlineItem(new QualityHeadViewItem(head_image[0], headline_title[1], headline_background[1], textColor));
                qualityHeadViewAdapter.addHeadlineItem(new QualityHeadViewItem(head_image[1], headline_title[2], headline_background[2], textColor));
                qualityHeadViewAdapter.addHeadlineItem(new QualityHeadViewItem(head_image[2], headline_title[3], headline_background[3], textColor));
                qualityHeadViewAdapter.addHeadlineItem(new QualityHeadViewItem(head_image[3], headline_title[4], headline_background[4], textColor));
                qualityHeadViewAdapter.addHeadlineItem(new QualityHeadViewItem(head_image[4], headline_title[5], headline_background[5], textColor));
                qualityHeadViewAdapter.addHeadlineItem(new QualityHeadViewItem(head_image[5], headline_title[6], headline_background[6], textColor));

                //卡片效果设置
                ViewPager viewPager = Objects.requireNonNull(getView()).findViewById(R.id.quality_head_view);
                QualityHeadViewTransformer qualityHeadViewTransformer = new QualityHeadViewTransformer(viewPager, qualityHeadViewAdapter);
                viewPager.setAdapter(qualityHeadViewAdapter);
                viewPager.setCurrentItem(0);//定义起始页
                viewPager.setPageTransformer(false, qualityHeadViewTransformer);//设置切换动画
                viewPager.setOffscreenPageLimit(5);
                qualityHeadViewTransformer.enableScaling();

                //设置内容显示
                final TextView textView_context = getView().findViewById(R.id.quality_context);
                //设置内容背景和文字
                final CustomPaintDrawable customPaintDrawable = new CustomPaintDrawable();
                final FrameLayout frameLayout = getView().findViewById(R.id.quality_context_layout);
                frameLayout.setBackground(customPaintDrawable.paintDrawable(48, headline_background[1]));
                textView_context.setTextColor(Color.parseColor(textColor));
                textView_context.setText(headline_content[1].replace("&&", "\n"));

                progressDialog.dismiss();

                TextView quality_blow_text = getView().findViewById(R.id.quality_blow_text);
                quality_blow_text.setText("新闻数据由iDtaApi提供");
                CardView quality_blow_card = getView().findViewById(R.id.quality_blow_card);
                quality_blow_card.setCardBackgroundColor(Color.parseColor("#FFF0A2"));

                //监听View滑动
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    float data_set = 0;
                    @Override
                    public void onPageScrolled(int i, float v, int i1) {
                        if (data_set < v){
                            int data = 100 - (int) (v * 100);
                            String my_data = String.valueOf(data);
                            String hex = toHex(my_data);
                            textView_context.setTextColor(Color.parseColor("#" + hex + "FFFFFF"));
                            data_set =v;
                            if (data <= 6){
                                textView_context.setTextColor(Color.parseColor("#FFFFFF"));
                            }
                        }else {
                            int data = (int) (v * 100);
                            String my_data = String.valueOf(data);
                            String hex = toHex(my_data);
                            textView_context.setTextColor(Color.parseColor("#" + hex + "FFFFFF"));
                            data_set =v;
                        }
                        if (v == 0.0){
                            textView_context.setTextColor(Color.parseColor("#FFFFFF"));
                        }
                    }

                    @Override
                    public void onPageSelected(int i) {
                        frameLayout.setBackground(customPaintDrawable.paintDrawable(48, headline_background[i + 1]));
                        textView_context.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.textview_in));
                        textView_context.setText(headline_content[i + 1].replace("&&", "\n"));
                    }

                    @Override
                    public void onPageScrollStateChanged(int i) {

                    }
                });
            }
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String data = null;

        //允许主线程网络请求
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

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
            headline_title = data_headline[0].split("/%/");
            headline_content = data_headline[1].split("/%/");
            headline_background = data_headline[2].split("/%/");
            headline_id = data_headline[3].split("/%/");
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

    private String toHex(String i) {
        System.out.println("透明度百分比对应的十六进制:");
        int j = Integer.parseInt(i);
        float temp = 255 * j * 1.0f / 100f;
        int round = Math.round(temp);
        String hexString = Integer.toHexString(round);
        if (hexString.length() < 2) {
            hexString = "0" + hexString;
        }
        return hexString;
    }
}
