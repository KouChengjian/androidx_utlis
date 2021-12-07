package com.example.kernel.ui.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.example.kernel.R;
import com.yiciyuan.kernel.widget.recycle.BaseRecyclerAdapter;
import com.yiciyuan.kernel.widget.recycle.BaseRecyclerViewHolder;

import java.util.List;

public class RecyclerTestAdapter extends BaseRecyclerAdapter<String, RecyclerTestAdapter.ViewHolder> {

    public RecyclerTestAdapter(Context mContext) {
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
