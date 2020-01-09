package com.socroty.zhifounews;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

public class CustomPaintDrawable {
    GradientDrawable paintDrawable(int i, String rgb){
        final GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(i);
        drawable.setColor(Color.parseColor(rgb));
        return drawable;
    }
}
