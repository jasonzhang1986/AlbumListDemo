package me.jifengzhang.albumlistdemo;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Author: Jifeng Zhang
 * Email : jifengzhang.barlow@gmail.com
 * Date  : 2017/5/17
 * Desc  :
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space, spanCount = 1;
    public SpaceItemDecoration(int space, int spanCount) {
        this.space = space;
        this.spanCount = spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.right = space;
        outRect.bottom = space;
//        if (parent.getChildLayoutPosition(view) % spanCount == 0) {
//            outRect.left = 0;
//        }
//        int lastLineCount = parent.getAdapter().getItemCount() % spanCount;
//        if(lastLineCount==0) {
//            lastLineCount = spanCount;
//        }
//        if (parent.getChildLayoutPosition(view)>= parent.getAdapter().getItemCount()- lastLineCount) {
//            outRect.bottom = (int)(space * 1.8);
//        }
    }
}
