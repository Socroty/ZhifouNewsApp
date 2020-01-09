package com.socroty.zhifounews;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class HomeFragmentAdapter extends FragmentPagerAdapter {

    private String[] mTitles = new String[]{"Tab_1", "Tab_2", "Tab_3"};

    HomeFragmentAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 1)
            return new VarietyFragment();
        else if (position == 2)
            return new PersonalFragment();
        return new QualityFragment();
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
