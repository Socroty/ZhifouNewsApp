package com.socroty.zhifounews;

import android.graphics.drawable.Drawable;

public class QualityHeadViewItem {
    private Drawable mImage;

    QualityHeadViewItem(Drawable image){
        mImage = image;
    }

    Drawable getItemImage() {
        return mImage;
    }
}
