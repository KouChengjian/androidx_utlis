package com.yiciyuan.widget.recycle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Administrator on 2017/12/13.
 */
public abstract class BaseRecyclerViewHolder<T> extends RecyclerView.ViewHolder {

    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
    }

    public void bindData(Context context, final T entity) {
    }

    public void bindData(Context context, final T entity, int position) {
    }

    public void bindData(Context context, final List<T> list, int position) {
    }

    public View getItemView(Context context, int rid, ViewGroup parent) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        return mLayoutInflater.inflate(rid, parent, false);
    }
}
