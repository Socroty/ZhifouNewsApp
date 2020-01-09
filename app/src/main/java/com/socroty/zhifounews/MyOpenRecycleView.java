package com.socroty.zhifounews;

import android.content.Context;
import android.util.AttributeSet;
import androidx.recyclerview.widget.RecyclerView;

public class MyOpenRecycleView extends RecyclerView {

    public MyOpenRecycleView(Context context) {
        super(context);
    }

    public MyOpenRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyOpenRecycleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
