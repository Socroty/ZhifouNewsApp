package com.socroty.zhifounews;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class MyWareView extends View {

    private Paint mPaint;
    private Path mPath;
    private int mItemWaveLength = 1600;
    private int dx;

    @SuppressLint("Recycle")
    public MyWareView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyWareView);
        String wareColor = typedArray.getString(R.styleable.MyWareView_wareColor);
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor(wareColor));
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        startAnim();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        int originY = 600;
        int halfWaveLen = mItemWaveLength / 2;
        //将mPath的起始位置向左移一个波长
        mPath.moveTo(-mItemWaveLength - dx, originY);
        //利用for循环画出当前屏幕中可能容得下的所有波
        for (int i = -mItemWaveLength; i <= getWidth() + mItemWaveLength; i += mItemWaveLength) {
            mPath.rQuadTo(halfWaveLen / 2, -50, halfWaveLen, 0); //一个波长中的前半个波
            mPath.rQuadTo(halfWaveLen / 2, 50, halfWaveLen, 0);  //一个波长中的后半个波
        }

        mPath.lineTo(getWidth(), getHeight());
        mPath.lineTo(0, getHeight());
        mPath.close();
        canvas.drawPath(mPath, mPaint);
    }

    private void startAnim() {
        ValueAnimator animator = ValueAnimator.ofInt(0, mItemWaveLength);
        animator.setDuration(1400);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dx = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.start();
    }
}
