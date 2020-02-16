package com.socroty.zhifounews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class MyHeiTiFontsTextView extends TextView {
    public MyHeiTiFontsTextView(Context context) {
        super(context);
    }

    public MyHeiTiFontsTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/SourceHanSansSC-Medium.otf");
        super.setTypeface(customFont);
    }
}
