package me.jifengzhang.albumlistdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Author: Jifeng Zhang
 * Email : jifengzhang.barlow@gmail.com
 * Date  : 2017/5/17
 * Desc  :
 */

public class CustomRecycleView extends RecyclerView {
    private int space = 0;
    public CustomRecycleView(Context context) {
        super(context);
        init();
    }

    public CustomRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    private void init() {
        //启用子视图排序功能
        setChildrenDrawingOrderEnabled(true);
    }

    public void setSpace(int space) {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager==null || !(layoutManager instanceof GridLayoutManager)) {
            throw new IllegalStateException("GridLayoutManager must be set!!");
        }
        this.space = space;
        if (space>0) {
            addItemDecoration(new SpaceItemDecoration(space, ((GridLayoutManager) layoutManager).getSpanCount()));
        }
    }
    @Override
    public void smoothScrollBy(int dx, int dy) {
        View focusView = getFocusedChild();
        if (focusView==null) {
            super.smoothScrollBy(dx, dy);
        } else {
            int itemHeight = focusView.getMeasuredHeight();
            int visibleCompleteLine = getMeasuredHeight() / (itemHeight + space);
            int distance = (itemHeight + space) * visibleCompleteLine;
            if (dy > 0) {
                super.smoothScrollBy(dx, distance);
            } else if (dy < 0) {
                super.smoothScrollBy(dx, -distance);
            } else {
                super.smoothScrollBy(dx, dy);
            }
        }
    }
    private int mSelectedPosition = 0;
    @Override
    public void onDraw(Canvas c) {
        mSelectedPosition = getChildAdapterPosition(getFocusedChild());
        super.onDraw(c);
    }
    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        int position = mSelectedPosition;
        if (position < 0) {
            return i;
        } else {
            if (i == childCount - 1) {
                if (position > i) {
                    position = i;
                }
                return position;
            }
            if (i == position) {
                return childCount - 1;
            }
        }
        return i;
    }

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
            int lastLineCount = parent.getAdapter().getItemCount() % spanCount;
            if(lastLineCount==0) {
                lastLineCount = spanCount;
            }
            if (parent.getChildLayoutPosition(view)>= parent.getAdapter().getItemCount()- lastLineCount) {
                int recycleHeight = parent.getMeasuredHeight();
                int viewHeight = view.getMeasuredHeight();
                int bottomSpace = recycleHeight % (viewHeight+space);
                Log.i("SpaceItemDecoration","recycleHeight = " + recycleHeight + ", viewHeight = " + viewHeight + ", bottomSpace = " + bottomSpace);
                outRect.bottom = bottomSpace + space;
            }
        }
    }
}
