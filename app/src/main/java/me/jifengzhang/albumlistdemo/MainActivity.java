package me.jifengzhang.albumlistdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnFocusChange;

public class MainActivity extends Activity implements View.OnFocusChangeListener {
    @BindView(R.id.layout_app) RelativeLayout mLayoutApp;
    @BindView(R.id.layout_left_root) LinearLayout mFirstCategoryRootView;
    @BindView(R.id.layout_right_root) RelativeLayout mRightRoot;
    @BindView(R.id.tv_app) TextView mText_App;
    @BindView(R.id.tv_game) TextView mText_Game;
    @BindView(R.id.tv_music) TextView mText_Music;
    @BindView(R.id.tv_parts) TextView mText_Parts;
    @BindView(R.id.tv_title) TextView mText_Title;
    @BindView(R.id.tv_title_right) TextView mText_Title_Right;

    @BindView(R.id.list_sub_category) RecyclerView mSubCategoryList;
    @BindView(R.id.list_content) GridRecycleView mContentList;

    private View mLastSelectItem = null;
    private static final int COLUMN_COUNT = 5;
    private String[] data = {"工具","生活","亲子","健康","教育","阅读","音乐","图像","工具","生活","亲子","健康","教育","阅读","音乐","图像","工具","生活","亲子","健康","教育","阅读","音乐","图像"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mCurViewInFirstCategory = mLayoutApp;
        mSubCategoryList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        SecCategoryAdapter adapter = new SecCategoryAdapter();
        adapter.setData(Arrays.asList(data));
        adapter.setOnFocusChangeListener(this);
        mSubCategoryList.setAdapter(adapter);

        mContentList.setLayoutManager(new GridLayoutManager(this, COLUMN_COUNT));
        int space = getResources().getDimensionPixelOffset(R.dimen.dp_20);
        mContentList.setSpace(space);
        mContentList.setScrollByPage(true);
        ContentListAdapter contentListAdapter = new ContentListAdapter();
        mContentList.setAdapter(contentListAdapter);

        getWindow().setBackgroundDrawable(null);

    }

    @OnFocusChange({R.id.layout_app, R.id.layout_game, R.id.layout_music, R.id.layout_parts, R.id.list_sub_category, R.id.list_content})
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            switch (v.getId()) {
                case R.id.layout_sub_album_root:
                    onFirstCategoryLayout(false);
                    if (mCurViewInSubCategoryList !=null && mCurViewInSubCategoryList !=v) {
                        mCurViewInSubCategoryList.setSelected(false);
                    }
                    mCurViewInSubCategoryList = v;
                    mCurViewInSubCategoryList.setSelected(true);
                    break;
                case R.id.layout_app:
                case R.id.layout_game:
                case R.id.layout_music:
                case R.id.layout_parts:
                    v.setSelected(true);
                    mCurViewInFirstCategory = v;
                    if (mLastSelectItem!=null) {
                        mLastSelectItem.setSelected(false);
                    }
                    mLastSelectItem = v;
                    onFirstCategoryLayout(true);
                    break;
            }
        }
    }
    private boolean mFocusInFirstCategory = true; //一级分类是否有焦点
    private View mCurViewInFirstCategory; //当前选中的一级分类view
    private View mCurViewInSubCategoryList; //当前选中的二级分类view
    private void onFirstCategoryLayout(boolean hasFocus) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mFirstCategoryRootView.getLayoutParams();
        if (mFocusInFirstCategory == hasFocus) {
            return;
        }
        params.width = hasFocus ? getResources().getDimensionPixelOffset(R.dimen.dp_360)
                    : getResources().getDimensionPixelOffset(R.dimen.dp_100);
        mText_App.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
        mText_Music.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
        mText_Game.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
        mText_Parts.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
        mText_Title.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
        mText_Title_Right.setVisibility(hasFocus ? View.GONE : View.VISIBLE);
        ((ViewGroup) mCurViewInFirstCategory).getChildAt(0).setVisibility(hasFocus ? View.GONE : View.VISIBLE);
        mFirstCategoryRootView.setLayoutParams(params);
        mFocusInFirstCategory = hasFocus;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            int keycode = event.getKeyCode();
            //一级分类有焦点，此时press right key
            if (keycode == KeyEvent.KEYCODE_DPAD_RIGHT && mFirstCategoryRootView.hasFocus()) {
                mSubCategoryList.getChildAt(0).requestFocus();
                return true;
            }
            //二级分类有焦点，并且焦点在第一个item上，此时press left key
            else if (keycode == KeyEvent.KEYCODE_DPAD_LEFT && mSubCategoryList.getChildLayoutPosition(mSubCategoryList.getFocusedChild())==0) {
                mCurViewInFirstCategory.requestFocus();
                return true;
            }
            //二级分类有焦点，并且焦点在第一个item上，此时press left key
            else if (keycode == KeyEvent.KEYCODE_DPAD_DOWN && mSubCategoryList.hasFocus()) {
                mContentList.getChildAt(0).requestFocus();
                return true;
            }
            //内容区域有焦点，并且焦点在第一行数据上，此时press up key
            else if (keycode == KeyEvent.KEYCODE_DPAD_UP && mContentList.hasFocus()) {
                int focusIndex = mContentList.getChildLayoutPosition(mContentList.getFocusedChild());
                if (focusIndex < COLUMN_COUNT && mCurViewInSubCategoryList !=null) {
                    mCurViewInSubCategoryList.requestFocus();
                    return true;
                }
            }
            //内容区域有焦点，并且焦点在第一列数据上，此时press left key
            else if (keycode == KeyEvent.KEYCODE_DPAD_LEFT && mContentList.hasFocus()) {
                int focusIndex = mContentList.getChildLayoutPosition(mContentList.getFocusedChild());
                if (focusIndex % COLUMN_COUNT == 0) {
                    mCurViewInFirstCategory.requestFocus();
                    return true;
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
