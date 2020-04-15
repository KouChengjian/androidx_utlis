package com.yiciyuan.widget.recycle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by Administrator on 2017/12/11.
 */
public abstract class BaseRecyclerAdapter<T, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {

    protected Context mContext;
    protected List<T> mList;
    protected LayoutInflater mLayoutInflater;

    public BaseRecyclerAdapter(Context mContext) {
        this(mContext, new ArrayList<T>());
    }

    public BaseRecyclerAdapter(Context mContext, List mList) {
        this.mContext = mContext;
        this.mList = mList;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void add(T object) {
        this.mList.add(object);
    }

    public void del(T object) {
        this.mList.remove(object);
    }

    public void del(int index) {
        this.mList.remove(index);
    }

    public void addAll(List mList) {
        this.mList.addAll(mList);
    }

    public void addAll(int position, List mList) {
        this.mList.addAll(position, mList);
    }

    public void setList(List mList) {
        this.mList = mList;
    }

    public List<T> getList() {
        return mList;
    }

    public Context getContext() {
        return mContext;
    }

    protected int getViewId() {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getItemView(ViewGroup parent) {
        return mLayoutInflater.inflate(getViewId(), parent, false);
    }

    public View getItemView(int rid, ViewGroup parent) {
        return mLayoutInflater.inflate(rid, parent, false);
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public V onCreateViewHolder(ViewGroup parent, int viewType) {
        return createView(parent, viewType);
    }

    @Override
    public void onBindViewHolder(V holder, int position) {
        bindView(holder, position);
    }

    public abstract V createView(ViewGroup parent, int viewType);

    public abstract void bindView(V holder, int position);
}
