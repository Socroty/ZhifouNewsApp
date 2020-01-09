package com.socroty.zhifounews;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyPassLinearLayoutManager extends LinearLayoutManager {

    MyPassLinearLayoutManager(Context context) {
        super( context );
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            //try catch一下
            super.onLayoutChildren( recycler, state );
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

}
