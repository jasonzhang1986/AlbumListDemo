package me.jifengzhang.albumlistdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

/**
 * Author: Jifeng Zhang
 * Email : jifengzhang.barlow@gmail.com
 * Date  : 2017/5/17
 * Desc  : 自定义的GridLayout类型的RecycleView，支持按行滚动和按页滚动
 */

public class GridRecycleView extends RecyclerView {
    private int space = 0; //item之间的间隔
    private boolean scrollByPage; // 是否整屏滚动
    public GridRecycleView(Context context) {
        this(context, null);
    }

    public GridRecycleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridRecycleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    private void init() {
        //启用子视图排序功能
        setChildrenDrawingOrderEnabled(true);
    }

    /**
     * 设置item之间的间隔
     * @param space 间隔大小(像素)
     */
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
    public void setScrollByPage(boolean scrollByPage) {
        this.scrollByPage = scrollByPage;
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
                if (scrollByPage) {
                    int visibleCompleteLine = getMeasuredHeight() / (itemHeight + space);
                    dy = (itemHeight + space) * visibleCompleteLine * symbol;
                } else {
                    dy = (itemHeight + space) * symbol;
                }
            } else if (dy == 0) { //横向滚动
                int symbol = dx>0?1:-1;
                if (scrollByPage) {
                    int visibleCompleteColumn = getMeasuredWidth() / (itemWidth + space);
                    dx = (itemWidth + space) * visibleCompleteColumn * symbol;
                } else {
                    dx = (itemWidth + space) * symbol;
                }
            }
        }
        super.smoothScrollBy(dx, dy);
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return getScrollState()!=SCROLL_STATE_IDLE || super.dispatchKeyEvent(event);
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

    private final static class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int space, spanCount = 1;
        SpaceItemDecoration(int space, int spanCount) {
            this.space = space;
            this.spanCount = spanCount;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.right = space;
            outRect.bottom = space;

            GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
            if (layoutManager.getOrientation() == RecyclerView.VERTICAL) {
                int lastLineCount = parent.getAdapter().getItemCount() % spanCount;
                if (lastLineCount == 0) {
                    lastLineCount = spanCount;
                }
                if (parent.getChildLayoutPosition(view) >= parent.getAdapter().getItemCount() - lastLineCount) {
                    int recycleHeight = parent.getMeasuredHeight();
                    int viewHeight = view.getMeasuredHeight();
                    int bottomSpace = recycleHeight % (viewHeight + space);
                    Log.i("SpaceItemDecoration", "recycleHeight = " + recycleHeight + ", viewHeight = " + viewHeight + ", bottomSpace = " + bottomSpace);
                    outRect.bottom = bottomSpace + space;
                }
            } else {
                int lastColumnCount = parent.getAdapter().getItemCount() % spanCount;
                if (lastColumnCount == 0) {
                    lastColumnCount = spanCount;
                }
                if (parent.getChildLayoutPosition(view) >= parent.getAdapter().getItemCount() - lastColumnCount) {
                    int recycleWidth = parent.getMeasuredWidth();
                    int viewWidth = view.getMeasuredWidth();
                    int rightSpace = recycleWidth % (viewWidth + space);
                    Log.i("SpaceItemDecoration", "recycleWidth = " + recycleWidth + ", viewWidth = " + viewWidth + ", rightSpace = " + rightSpace);
                    outRect.right = rightSpace + space;
                }
            }
        }
    }
}
