package com.linkplayer.linkplayer.fragment;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class LinearVerticalSpacing extends RecyclerView.ItemDecoration {

    private final int verticalSpaceHeight;

    public LinearVerticalSpacing(int verticalSpaceHeight) {
        this.verticalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {

        outRect.top = verticalSpaceHeight;

    }
}
