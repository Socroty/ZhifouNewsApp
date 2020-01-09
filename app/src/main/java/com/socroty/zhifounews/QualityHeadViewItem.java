package com.socroty.zhifounews;

import android.graphics.drawable.Drawable;

public class QualityHeadViewItem {
    private Drawable mImage;
    private String mText;
    private String mBackgroundColor;
    private String mTextColor;

    QualityHeadViewItem(Drawable image, String text, String backgroundColor, String textColor){
        mImage = image;
        mText = text;
        mBackgroundColor = backgroundColor;
        mTextColor = textColor;
    }

    Drawable getItemImage() {
        return mImage;
    }

    String getItemText(){
        return mText;
    }

    String getItemBackgroundColor(){
        return mBackgroundColor;
    }

    String getItemTextColor(){
        return mTextColor;
    }
}
