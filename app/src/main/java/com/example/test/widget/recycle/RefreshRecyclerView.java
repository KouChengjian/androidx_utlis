package com.example.test.widget.recycle;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.example.test.R;
import com.example.test.widget.TipLayoutView;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class RefreshRecyclerView extends FrameLayout {

    private View mHeaderView;
    private View mFooterView;
    private LayoutInflater layoutInflater;
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;
    private MaterialHeader materialHeader;
    private ClassicsFooter classicsFooter;
    private TipLayoutView tipLayoutView;
    private IWrapAdapter wrapAdapter;

    public RefreshRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public RefreshRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.include_recyclerview, this);

        smartRefreshLayout = view.findViewById(R.id.smartRefreshLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        materialHeader = view.findViewById(R.id.materialHeader);
        classicsFooter = view.findViewById(R.id.classicsFooter);
        tipLayoutView = view.findViewById(R.id.tip_layout_view);
    }

    public LinearLayoutManager initLinearLayoutManager() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        setLayoutManager(layoutManager);
        return layoutManager;
    }

    public LinearLayoutManager initHorizontalLayoutManager() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        setLayoutManager(layoutManager);
        return layoutManager;
    }

    public StaggeredGridLayoutManager initStaggeredGridLayoutManager(int sum) {
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(sum, StaggeredGridLayoutManager.VERTICAL);
        setLayoutManager(staggeredGridLayoutManager);
        return staggeredGridLayoutManager;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        recyclerView.setLayoutManager(layout);
    }

    public void addHeader(View view) {
        addHeader(view, true);
    }

    public void addHeader(View view, boolean isShow) {
        mHeaderView = view;
        if (!isShow) { // 初始化是否隐藏
            mHeaderView.setVisibility(View.GONE);
        }
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        wrapAdapter = new IWrapAdapter(mHeaderView, mFooterView, adapter);
        recyclerView.setAdapter(wrapAdapter);
    }

    public void setEnabelItemAnima(boolean enable) {
        if (enable) {
            openDefaultAnimator();
        } else {
            closeDefaultAnimator();
        }
    }

    public void setRefreshEnabled(boolean enabled) {
        smartRefreshLayout.setEnableRefresh(enabled);
    }

    public void setLoadMoreEnabled(boolean enabled) {
        smartRefreshLayout.setEnableLoadMore(enabled);
    }

    public void finishRefresh() {
        smartRefreshLayout.finishRefresh();
    }

    public void finishRefresh(int delayed) {
        smartRefreshLayout.finishRefresh(delayed);
    }

    public void finishRefresh(final int delayed, final boolean success, final Boolean noMoreData) {
        smartRefreshLayout.finishRefresh(delayed, success, noMoreData);
        smartRefreshLayout.setEnableRefresh(false);
    }

    public void finishLoadMore() {
        smartRefreshLayout.finishLoadMore();
    }

    public void finishLoadMore(int delayed) {
        smartRefreshLayout.finishLoadMore(delayed);
        smartRefreshLayout.setNoMoreData(true); // 提示没有更多数据
//        smartRefreshLayout.setEnableLoadMore(false); // 禁止滑动
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        smartRefreshLayout.setOnRefreshListener(onRefreshListener);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        smartRefreshLayout.setOnLoadMoreListener(onLoadMoreListener);
    }

    public void setOnReloadListener(OnReloadListener onReloadListener) {
    }

    public void addOnItemTouchListener(RecyclerView.OnItemTouchListener onItemTouchListener) {
        recyclerView.addOnItemTouchListener(onItemTouchListener);
    }

    public void addOnScrollListener(RecyclerView.OnScrollListener listener) {
        recyclerView.addOnScrollListener(listener);
    }

    public void removeOnScrollListener(RecyclerView.OnScrollListener listener) {
        recyclerView.removeOnScrollListener(listener);
    }

    public void setOnTouchListener(View.OnTouchListener onTouchListener) {
        recyclerView.setOnTouchListener(onTouchListener);
    }

    public void setOnItemClickListener(final ItemClickSupport.OnItemClickListener mPresenter) {
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                //之所以再用ItemClickSupport.OnItemClickListener报装一层就是为了不能点到FooterView(目前是加载更多提示)
                if (position <wrapAdapter.getItemCount() - wrapAdapter.getHeadersCount()) {
                    if (mPresenter != null){
                        mPresenter.onItemClicked(recyclerView, position, v);
                    }
                }
            }
        });
    }

    public void setOnItemLongClickListener(final ItemClickSupport.OnItemLongClickListener mPresenter) {
        ItemClickSupport.addTo(recyclerView).setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                //之所以再用ItemClickSupport.OnItemClickListener报装一层就是就是为了不能点到FooterView(目前是加载更多提示)
                if (position <wrapAdapter.getItemCount() - wrapAdapter.getHeadersCount()) {
                    if (mPresenter != null) {
                        return mPresenter.onItemLongClicked(recyclerView, position, v);
                    }
                }
                return false;
            }
        });
    }









    /**
     * 打开默认局部刷新动画
     */
    private void openDefaultAnimator() {
        this.recyclerView.getItemAnimator().setAddDuration(120);
        this.recyclerView.getItemAnimator().setChangeDuration(250);
        this.recyclerView.getItemAnimator().setMoveDuration(250);
        this.recyclerView.getItemAnimator().setRemoveDuration(120);
        ((SimpleItemAnimator) this.recyclerView.getItemAnimator()).setSupportsChangeAnimations(true);
    }

    /**
     * 关闭默认局部刷新动画
     */
    private void closeDefaultAnimator() {
        this.recyclerView.getItemAnimator().setAddDuration(0);
        this.recyclerView.getItemAnimator().setChangeDuration(0);
        this.recyclerView.getItemAnimator().setMoveDuration(0);
        this.recyclerView.getItemAnimator().setRemoveDuration(0);
        ((SimpleItemAnimator) this.recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }
}
