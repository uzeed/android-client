package com.neverwin.uzeed.uzeed.Utils;

import android.content.Context;
import android.widget.LinearLayout;

public class SquareLayout extends LinearLayout {


    public SquareLayout(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        // or you can use this if you want the square to use height as it basis
        // super.onMeasure(heightMeasureSpec, heightMeasureSpec);
    }
}
