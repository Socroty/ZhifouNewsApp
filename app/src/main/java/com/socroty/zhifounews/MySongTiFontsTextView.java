package com.socroty.zhifounews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

//自定义字体TextView
@SuppressLint("AppCompatCustomView")
public class MySongTiFontsTextView extends TextView {

    public MySongTiFontsTextView(Context context) {
        super(context);
    }

    public MySongTiFontsTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/SourceHanSerifSC-Medium.otf");
        super.setTypeface(customFont);
    }
}
