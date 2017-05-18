package me.jifengzhang.albumlistdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    @BindView(R.id.layout_left_root) LinearLayout mLeftRoot;
    @BindView(R.id.layout_right_root) RelativeLayout mRightRoot;
    @BindView(R.id.tv_app) TextView mText_App;
    @BindView(R.id.tv_game) TextView mText_Game;
    @BindView(R.id.tv_music) TextView mText_Music;
    @BindView(R.id.tv_parts) TextView mText_Parts;
    @BindView(R.id.tv_title) TextView mText_Title;
    @BindView(R.id.tv_title_right) TextView mText_Title_Right;

    @BindView(R.id.list_secondary_classification) RecyclerView mSecondaryClassificationList;
    @BindView(R.id.list_content) CustomRecycleView mContentList;

    private View mLastSelectItem = null;
    private String[] data = {"工具","生活","亲子","健康","教育","阅读","音乐","图像","工具","生活","亲子","健康","教育","阅读","音乐","图像","工具","生活","亲子","健康","教育","阅读","音乐","图像"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mCurrentFirstClassificationView = mLayoutApp;
        mSecondaryClassificationList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        SecondaryClassificationAdapter adapter = new SecondaryClassificationAdapter();
        adapter.setData(Arrays.asList(data));
        adapter.setOnFocusChangeListener(this);
        mSecondaryClassificationList.setAdapter(adapter);

        mContentList.setLayoutManager(new GridLayoutManager(this, 5));
        int space = getResources().getDimensionPixelOffset(R.dimen.dp_20);
        Log.i("MainActivity","space = " + space);
        mContentList.setSpace(space);
        ContentListAdapter contentListAdapter = new ContentListAdapter();
        mContentList.setAdapter(contentListAdapter);

    }

    @OnFocusChange({R.id.layout_app, R.id.layout_game, R.id.layout_music, R.id.layout_parts, R.id.list_secondary_classification, R.id.list_content})
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            switch (v.getId()) {
                case R.id.layout_sub_album_root:
                    changeLeftFocus(false);
                    if (mCurrentSecondaryClassificationView!=null && mCurrentSecondaryClassificationView!=v) {
                        mCurrentSecondaryClassificationView.setSelected(false);
                    }
                    mCurrentSecondaryClassificationView = v;
                    mCurrentSecondaryClassificationView.setSelected(true);
                    break;
                case R.id.layout_app:
                case R.id.layout_game:
                case R.id.layout_music:
                case R.id.layout_parts:
                    v.setSelected(true);
                    mCurrentFirstClassificationView = v;
                    if (mLastSelectItem!=null) {
                        mLastSelectItem.setSelected(false);
                    }
                    mLastSelectItem = v;
                    changeLeftFocus(true);
                    break;
            }
        }
    }
    private boolean mLeftHasFocus = true; //一级分类是否有焦点
    private View mCurrentFirstClassificationView; //当前选中的一级分类view
    private View mCurrentSecondaryClassificationView; //当前选中的二级分类view
    private void changeLeftFocus(boolean hasFocus) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mLeftRoot.getLayoutParams();
        if (mLeftHasFocus == hasFocus) {
            return;
        }
        if (!hasFocus) {
            params.width = getResources().getDimensionPixelOffset(R.dimen.dp_100);
            mText_App.setVisibility(View.GONE);
            mText_Music.setVisibility(View.GONE);
            mText_Game.setVisibility(View.GONE);
            mText_Parts.setVisibility(View.GONE);
            mText_Title.setVisibility(View.GONE);
            mText_Title_Right.setVisibility(View.VISIBLE);
            ((ViewGroup)mCurrentFirstClassificationView).getChildAt(0).setVisibility(View.VISIBLE);
        } else {
            params.width = getResources().getDimensionPixelOffset(R.dimen.dp_360);
            mText_App.setVisibility(View.VISIBLE);
            mText_Music.setVisibility(View.VISIBLE);
            mText_Game.setVisibility(View.VISIBLE);
            mText_Parts.setVisibility(View.VISIBLE);
            mText_Title_Right.setVisibility(View.GONE);
            mText_Title.setVisibility(View.VISIBLE);
            ((ViewGroup)mCurrentFirstClassificationView).getChildAt(0).setVisibility(View.GONE);
        }
        mLeftRoot.setLayoutParams(params);
        mLeftHasFocus = hasFocus;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            int keycode = event.getKeyCode();
            //一级分类有焦点，此时press right key
            if (keycode == KeyEvent.KEYCODE_DPAD_RIGHT && mLeftRoot.hasFocus()) {
                mSecondaryClassificationList.getChildAt(0).requestFocus();
                return true;
            }
            //二级分类有焦点，并且焦点在第一个item上，此时press left key
            else if (keycode == KeyEvent.KEYCODE_DPAD_LEFT && mSecondaryClassificationList.getChildLayoutPosition(mSecondaryClassificationList.getFocusedChild())==0) {
                mCurrentFirstClassificationView.requestFocus();
                return true;
            }
            //二级分类有焦点，并且焦点在第一个item上，此时press left key
            else if (keycode == KeyEvent.KEYCODE_DPAD_DOWN && mSecondaryClassificationList.hasFocus()) {
                mContentList.getChildAt(0).requestFocus();
                return true;
            }
            //内容区域有焦点，并且焦点在第一行数据上，此时press up key
            else if (keycode == KeyEvent.KEYCODE_DPAD_UP && mContentList.hasFocus()) {
                int focusIndex = mContentList.getChildLayoutPosition(mContentList.getFocusedChild());
                if (focusIndex < 5 && mCurrentSecondaryClassificationView!=null) {
                    mCurrentSecondaryClassificationView.requestFocus();
                    return true;
                }
            }
            //内容区域有焦点，并且焦点在第一列数据上，此时press left key
            else if (keycode == KeyEvent.KEYCODE_DPAD_LEFT && mContentList.hasFocus()) {
                int focusIndex = mContentList.getChildLayoutPosition(mContentList.getFocusedChild());
                if (focusIndex % 5 == 0) {
                    mCurrentFirstClassificationView.requestFocus();
                    return true;
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
