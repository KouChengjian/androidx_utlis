package com.example.test.widget.recycle;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Created with Android Studio.
 * User: kcj
 * Date: 2019/1/15 16:53
 * Description:
 */
public class IWrapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = -4;
    private static final int TYPE_FOOTER = -3;
    private static final int TYPE_NORMAL = 0;

    private RecyclerView.Adapter adapter;
    private View mHeaderView;
    private View mFootView;

    public IWrapAdapter(View headerView, View footView, RecyclerView.Adapter adapter) {
        this.adapter = adapter;
        if (headerView != null) {
            this.mHeaderView = headerView;
        }
        if (footView != null) {
            this.mFootView = footView;
        }

        adapter.registerAdapterDataObserver(mDataObserver);
    }

    public int getHeadersCount() {
        return this.mHeaderView == null ? 0 : 1;
    }

    public int getFootersCount() {
        return this.mFootView == null ? 0 : 1;
    }

    public boolean isHeader(int position) {
        return position >= 0 && position < getHeadersCount();
    }

    public boolean isFooter(int position) {
        return position < getItemCount() && position >= getItemCount() - getFootersCount();
    }

    @Override
    public int getItemCount() {
        if (adapter != null) {
            return getHeadersCount() + getFootersCount() + adapter.getItemCount();
        } else {
            return getHeadersCount() + getFootersCount();
        }
    }

    @Override
    public long getItemId(int position) {
        if (adapter != null && position >= getHeadersCount()) {
            int adjPosition = position - getHeadersCount();
            int adapterCount = adapter.getItemCount();
            if (adjPosition < adapterCount) {
                return adapter.getItemId(adjPosition);
            }
        }
        return -1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeader(position)) {
            return TYPE_HEADER;
        }
        if (isFooter(position)) {
            return TYPE_FOOTER;
        }
        int adjPosition = position - getHeadersCount();

        int adapterCount;
        if (adapter != null) {
            adapterCount = adapter.getItemCount();
            if (adjPosition < adapterCount) {
                return adapter.getItemViewType(adjPosition);
            }
        }
        return TYPE_NORMAL;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (isHeader(position) || isFooter(position)) ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams &&
                (isHeader(holder.getLayoutPosition()) || isFooter(holder.getLayoutPosition()))) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return new SimpleViewHolder(mHeaderView);
        } else if (viewType == TYPE_FOOTER) {
            return new SimpleViewHolder(mFootView);
        }
        return adapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (isHeader(position)) {
            return;
        }
        int adjPosition = position - getHeadersCount();
        int adapterCount;
        if (adapter != null) {
            adapterCount = adapter.getItemCount();
            if (adjPosition < adapterCount) {
                adapter.onBindViewHolder(holder, adjPosition);
                return;
            }
        }
    }

    private class SimpleViewHolder extends RecyclerView.ViewHolder {
        public SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }

    private final RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            IWrapAdapter.this.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            positionStart += getHeadersCount();
            IWrapAdapter.this.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            positionStart += getHeadersCount();
            IWrapAdapter.this.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            positionStart += getHeadersCount();
            IWrapAdapter.this.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            int headersCount = getHeadersCount();
            fromPosition += headersCount;
            toPosition += headersCount;
            IWrapAdapter.this.notifyItemMoved(fromPosition, toPosition);
        }
    };
}
