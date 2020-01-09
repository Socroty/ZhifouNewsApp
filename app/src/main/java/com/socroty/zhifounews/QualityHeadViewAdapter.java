package com.socroty.zhifounews;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class QualityHeadViewAdapter extends PagerAdapter {

    private List<QualityHeadViewItem> mItems;
    private List<CardView> mViews;
    private float mBaseElevation = 0;
    private float mMaxElevation = 8;

    //构造方法
    QualityHeadViewAdapter() {
        mItems = new ArrayList<>();
        mViews = new ArrayList<>();
    }

    void addHeadlineItem(QualityHeadViewItem item) {
        mItems.add(item);
        mViews.add(null);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    float getBaseElevation() {
        return mBaseElevation;
    }

    float getMaxElevation(){
        return mMaxElevation;
    }

    @NonNull
    @Override
    //预加载视图内容
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.quality_head_view_card, container, false);
        container.addView(view);
        bind(mItems.get(position), view);
        CardView cardView = view.findViewById(R.id.quality_head_view_card);
        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }
        cardView.setMaxCardElevation(mBaseElevation * mMaxElevation);
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
        mViews.set(position,null);
    }

    private void bind(QualityHeadViewItem item, View view) {
        ImageView imageView = view.findViewById(R.id.quality_head_view_card_image);
        imageView.setImageDrawable(item.getItemImage());

        TextView textView = view.findViewById(R.id.quality_head_view_card_text);
        textView.setText(item.getItemText());

        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(40);
        drawable.setColor(Color.parseColor(item.getItemBackgroundColor()));

        textView.setBackground(drawable);
        textView.setTextColor(Color.parseColor(item.getItemTextColor()));

    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
