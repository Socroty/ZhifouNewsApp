package com.socroty.zhifounews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class MyFontsTextView extends TextView {

    public MyFontsTextView(Context context) {
        super(context);
    }

    public MyFontsTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/MyChineseFont.TTF");
        super.setTypeface(customFont);
    }
}
