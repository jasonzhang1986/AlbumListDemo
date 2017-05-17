package me.jifengzhang.albumlistdemo;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: Jifeng Zhang
 * Email : jifengzhang.barlow@gmail.com
 * Date  : 2017/5/17
 * Desc  :
 */

public class ContentListAdapter extends RecyclerView.Adapter<ContentListAdapter.ViewHolder>{

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(parent.getContext(),R.layout.item_content,null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.appName.setText("知乎"+position);
        Log.i("ContentListAdapter","onBindViewHolder ["+holder.itemView.getMeasuredWidth()+","+holder.itemView.getMeasuredHeight()+"]");
    }

    @Override
    public int getItemCount() {
        return 50;
    }

    final class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_app) ImageView appIcon;
        @BindView(R.id.tv_app_name) TextView appName;
        @BindView(R.id.tv_app_download_count) TextView appDownloadCount;
        @BindView(R.id.tv_app_size) TextView appSize;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnFocusChangeListener(onFocusChangeListener);
        }
        private View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    AnimationUtil.startScaleToBigAnimation(v, 1.15f,null);
                } else {
                    AnimationUtil.startScaleToSmallAnimation(v, 1.15f, null);
                }
            }
        };
    }
}
