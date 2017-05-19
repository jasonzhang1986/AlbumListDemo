package me.jifengzhang.albumlistdemo;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Author: Jifeng Zhang
 * Email : jifengzhang.barlow@gmail.com
 * Date  : 2017/5/18
 * Desc  :
 */

public class LinearRecycleView extends RecyclerView {
    private int space = 0; //item之间的间隔
    private boolean scrollByPage; // 是否整屏滚动
    public LinearRecycleView(Context context) {
        this(context, null);
    }

    public LinearRecycleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinearRecycleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setSpace(int space) {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager==null || !(layoutManager instanceof  LinearLayoutManager)) {
            throw new IllegalStateException("LinearLayoutManager must be set!!");
        }
        if (space != 0 ) {
            this.space = space;
            addItemDecoration(new SpaceItemDecoration(space));
        }
    }

    private int mItemWidth, mItemHeight;
    private int getItemWidth() {
        if (mItemWidth==0) {
            View focusView = getFocusedChild();
            if (focusView!=null) {
                mItemWidth = focusView.getMeasuredWidth();
            }
        }
        return mItemWidth;
    }
    private int getItemHeight() {
        if (mItemHeight==0) {
            View focusView = getFocusedChild();
            if (focusView!=null) {
                mItemHeight = focusView.getMeasuredHeight();
            }
        }
        return mItemHeight;
    }

    @Override
    public void smoothScrollBy(int dx, int dy) {
        if (getFocusedChild()!= null) {
            int itemWidth = getItemWidth();
            int itemHeight = getItemHeight();
            if (dx == 0) { //纵向滚动
                int symbol = dy>0?1:-1;
                dy = (itemHeight + space) * symbol;
            } else if (dy == 0) { //横向滚动
                int symbol = dx>0?1:-1;
                dx = (itemWidth + space) * symbol;
            }
        }
        super.smoothScrollBy(dx, dy);
    }

    private final static class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int space;
        SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
            if (layoutManager.getOrientation() == RecyclerView.HORIZONTAL) {
                outRect.right = space;
            } else {
                outRect.bottom = space;
            }
        }
    }
}
