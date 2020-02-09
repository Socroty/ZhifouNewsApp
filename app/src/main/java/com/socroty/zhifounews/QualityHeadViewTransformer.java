package com.socroty.zhifounews;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

public class QualityHeadViewTransformer implements ViewPager.OnPageChangeListener, ViewPager.PageTransformer {
    private ViewPager mViewPager;
    private float mLastOffset;
    private boolean mScalingEnabled;
    private QualityHeadViewAdapter qualityHeadViewAdapter;

    QualityHeadViewTransformer(ViewPager viewPager, QualityHeadViewAdapter adapter) {
        mViewPager = viewPager;
        viewPager.addOnPageChangeListener(this);
        qualityHeadViewAdapter = adapter;
    }

    void enableScaling() {
        if (mScalingEnabled) {
            CardView currentCard = qualityHeadViewAdapter.getCardViewAt(mViewPager.getCurrentItem());
            if (currentCard != null) {
                currentCard.animate().scaleY(1);
                currentCard.animate().scaleX(1);
            }
        }
        mScalingEnabled = true;
    }

    @Override
    public void onPageScrolled(int position, float positionOffSet, int positionOffSetPixels) {
        int realCurrentPosition;
        int nextPosition;
        float baseElevation = qualityHeadViewAdapter.getBaseElevation();
        float realOffset;
        boolean goingLeft = mLastOffset > positionOffSet;

        if (goingLeft) {
            realCurrentPosition = position + 1;
            nextPosition = position;
            realOffset = 1 - positionOffSet;
        } else {
            nextPosition = position + 1;
            realCurrentPosition = position;
            realOffset = positionOffSet;
        }

        if (nextPosition > qualityHeadViewAdapter.getCount() - 1
                || realCurrentPosition > qualityHeadViewAdapter.getCount() - 1) {
            return;
        }

        CardView currentCard = qualityHeadViewAdapter.getCardViewAt(realCurrentPosition);

        if (currentCard != null) {
            if (mScalingEnabled) {
                currentCard.setScaleX((float) (1 + 0.1 * (1 - realOffset)));
                currentCard.setScaleY((float) (1 + 0.1 * (1 - realOffset)));
            }
            currentCard.setCardElevation((baseElevation + baseElevation
                    * (qualityHeadViewAdapter.getMaxElevation() - 1) * (1 - realOffset)));
        }

        CardView nextCard = qualityHeadViewAdapter.getCardViewAt(nextPosition);

        if (nextCard != null) {
            if (mScalingEnabled) {
                nextCard.setScaleX((float) (1 + 0.1 * (realOffset)));
                nextCard.setScaleY((float) (1 + 0.1 * (realOffset)));
            }
            nextCard.setCardElevation((baseElevation + baseElevation
                    * (qualityHeadViewAdapter.getMaxElevation() - 1) * (realOffset)));
        }
        mLastOffset = positionOffSet;

    }

    @Override
    public void onPageSelected(int i) {
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void transformPage(@NonNull View view, float v) {

    }
}
