package com.socroty.zhifounews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("AppCompatCustomView")
public class MyAlignTextView extends TextView{

    private List<String> lines = new ArrayList<>(); // 分割后的行
    private List<Integer> tailLines = new ArrayList<>(); // 尾行

    private String oldText;

    public MyAlignTextView(Context context) {
        super(context);
        setTextIsSelectable(false);
    }

    public MyAlignTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTextIsSelectable(false);
        setCustomFont(context);
    }

    private void setCustomFont(Context context) {
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/MyChineseFont.TTF");
        super.setTypeface(customFont);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);   // 计算宽度
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (heightMode == MeasureSpec.EXACTLY) {
            // 确定高度，直接返回
            setMeasuredDimension(getMeasuredWidth(), heightSize);
        } else {
            reCalculate();

            int height = lines.size() * getLineHeight();

            if (heightMode ==  MeasureSpec.AT_MOST) {
                // 最小高度
                setMeasuredDimension(getMeasuredWidth(), Math.min(heightSize, height));
            } else {
                setMeasuredDimension(getMeasuredWidth(), height);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        TextPaint paint = getPaint();
        paint.setColor(getCurrentTextColor());
        paint.drawableState = getDrawableState();

        Paint.FontMetrics fm = paint.getFontMetrics();
        float firstHeight = getTextSize() - (fm.bottom - fm.descent + fm.ascent - fm.top);

        int gravity = getGravity();
        if ((gravity & 0x1000) == 0) { // 是否垂直居中
            firstHeight = firstHeight + (getTextSize() - firstHeight) / 2;
        }

        int paddingTop = getPaddingTop();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int width = getMeasuredWidth() - paddingLeft - paddingRight;

        for (int i = 0; i < lines.size(); i++) {
            float drawY = i * getLineHeight() + firstHeight;
            String line = lines.get(i);
            // 绘画起始x坐标
            float gap = (width - paint.measureText(line));
            float interval = gap / (line.length() - 1);

            // 绘制最后一行
            if (tailLines.contains(i)) {
                interval = 0;
            }

            for (int j = 0; j < line.length(); j++) {
                float drawX = paint.measureText(line.substring(0, j)) + interval * j;
                canvas.drawText(line.substring(j, j + 1), drawX + (float) paddingLeft, drawY + paddingTop, paint);
            }
        }
    }

    private void reCalculate() {
        String text = getText().toString();
        if (text.equals(oldText))
            return;
        oldText = text;

        TextPaint paint = getPaint();
        lines.clear();
        tailLines.clear();

        // 文本含有换行符时，分割单独处理
        String[] items = text.split("\\n");
        for (String item : items) {
            calculateText(paint, item);
        }
    }

    /**
     * 计算每行应显示的文本数
     *
     * @param text 要计算的文本
     */
    private void calculateText(Paint paint, String text) {
        if (text.length() == 0) {
            lines.add("\n");
            return;
        }
        int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int startPosition = 0; // 起始位置
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < text.length(); ++i) {
            if(paint.measureText(text.substring(startPosition, i + 1)) > (float) width) {
                startPosition = i;
                this.lines.add(sb.toString());
                sb = new StringBuilder();
                i--;
            } else {
                sb.append(text.charAt(i));
            }
        }

        if(sb.length() > 0) {
            lines.add(sb.toString());
        }
        tailLines.add(this.lines.size() - 1);
    }
}
