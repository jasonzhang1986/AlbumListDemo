package me.jifengzhang.albumlistdemo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Jifeng Zhang
 * Email : jifengzhang.barlow@gmail.com
 * Date  : 2017/5/16
 * Desc  : 二级分类的Adapter
 */

public class SecondaryClassificationAdapter extends RecyclerView.Adapter<SecondaryClassificationAdapter.ViewHolder>{
    private List<String> mData;
    private View.OnFocusChangeListener mOnFocusChangeListener;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(parent.getContext(),R.layout.item_secondarycla,null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.text.setText(mData.get(position));
        holder.itemView.setOnFocusChangeListener(mOnFocusChangeListener);
    }

    public void setOnFocusChangeListener(View.OnFocusChangeListener listener) {
        mOnFocusChangeListener = listener;
    }
    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void setData(List<String> data) {
        if (mData==null) {
            mData = new ArrayList<>();
        } else {
            mData.clear();
        }
        mData.addAll(data);
    }

    final class ViewHolder extends RecyclerView.ViewHolder{
        private TextView text;
        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.tv);
            itemView.setOnFocusChangeListener(mOnFocusChangeListener);
        }
    }
}
