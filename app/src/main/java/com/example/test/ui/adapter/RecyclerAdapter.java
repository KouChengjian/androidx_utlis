package com.example.test.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.example.test.R;
import com.example.test.widget.recycle.BaseRecyclerAdapter;
import com.example.test.widget.recycle.BaseRecyclerViewHolder;

import java.util.List;

public class RecyclerAdapter extends BaseRecyclerAdapter<String, RecyclerAdapter.ViewHolder> {

    public RecyclerAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public ViewHolder createView(ViewGroup parent, int viewType) {
        return new ViewHolder(getItemView(R.layout.item_test, parent));
    }

    @Override
    public void bindView(ViewHolder holder, int position) {
        holder.bindData(getContext(), getList().get(position));
    }

    public class ViewHolder extends BaseRecyclerViewHolder<String> {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(Context context, String entity) {
            super.bindData(context, entity);
        }

        @Override
        public void bindData(Context context, List<String> list, int position) {
            super.bindData(context, list, position);
        }
    }
}
