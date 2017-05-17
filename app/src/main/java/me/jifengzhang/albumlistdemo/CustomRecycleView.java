package me.jifengzhang.albumlistdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Author: Jifeng Zhang
 * Email : jifengzhang.barlow@gmail.com
 * Date  : 2017/5/17
 * Desc  :
 */

public class CustomRecycleView extends RecyclerView {
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
    private int offset = 0;
    public void setScrollOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public void smoothScrollBy(int dx, int dy) {
        Log.i("CustomRecycleView","smoothScrollBy " + getResources().getDimensionPixelOffset(R.dimen.dp_240));
        if (dy > 0) {
            super.smoothScrollBy(dx, getResources().getDimensionPixelOffset(R.dimen.dp_240)*2 +getResources().getDimensionPixelOffset(R.dimen.dp_20)*2);
        } else if (dy < 0) {
            super.smoothScrollBy(dx, -(getResources().getDimensionPixelOffset(R.dimen.dp_240)*2 +getResources().getDimensionPixelOffset(R.dimen.dp_20)*2));
        } else {
            super.smoothScrollBy(dx, dy);
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
}
