package com.yiciyuan.widget.recycle;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.yiciyuan.widget.R;
import com.yiciyuan.widget.TipLayoutView;
import com.yiciyuan.widget.recycle.listener.OnReloadListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class RefreshRecyclerView extends FrameLayout implements TipLayoutView.OnReloadClick {

    private Context context;
    private View mHeaderView;
    private View mFooterView;
    private LayoutInflater layoutInflater;
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;
    private ClassicsFooter classicsFooter;
    private TipLayoutView tipLayoutView;
    private IWrapAdapter wrapAdapter;

    private OnRefreshListener onRefreshListener;
    private OnReloadListener onReloadListener;

    private LayoutStatus layoutStatus = LayoutStatus.LAYOUT_STATUS_EMPTY;          // 默认的布局状态
    private int customImage = R.mipmap.bg_loading_no_wifi;
    private String customMessage = "亲，出现异常咯";
    private String customBtn = "刷新看看";

    private int mHeaderProgressColor = 0x000000;

    public RefreshRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public RefreshRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RefreshRecyclerView);
        mHeaderProgressColor = ta.getColor(R.styleable.RefreshRecyclerView_rrv_header_progressColor, mHeaderProgressColor);
        ta.recycle();
        initViews();
        initEvent();
        initDatas();
    }

    private void initViews() {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.include_recycler_view, this);

        smartRefreshLayout = view.findViewById(R.id.smartRefreshLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        classicsFooter = view.findViewById(R.id.classicsFooter);
        tipLayoutView = view.findViewById(R.id.tip_layout_view);
        RefreshHeader refreshHeader = smartRefreshLayout.getRefreshHeader();
        if (refreshHeader instanceof MaterialHeader) {
            ((MaterialHeader) refreshHeader).setColorSchemeColors(mHeaderProgressColor);
        }

    }

    private void initEvent() {
        tipLayoutView.setOnReloadClick(this);
    }

    private void initDatas() {

    }

    public SmartRefreshLayout getRefreshLayout() {
        return smartRefreshLayout;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public TipLayoutView getTipLayoutView() {
        return tipLayoutView;
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

    public void setLayoutStatus(LayoutStatus layoutStatus) {
        this.layoutStatus = layoutStatus;
    }

    public void setCustomLayoutStatus(int id, String customMessgae) {
        setCustomLayoutStatus(id, customMessgae, "");
    }

    public void setCustomLayoutStatus(int id, String customMessgae, String customBtn) {
        this.customImage = id;
        this.customMessage = customMessgae;
        this.customBtn = customBtn;
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

    public void autoRefresh() {
        smartRefreshLayout.autoRefresh();
        tipLayoutView.showContent();
    }

    public void finishRefresh() {
        smartRefreshLayout.finishRefresh();
    }

    public void finishRefresh(int delayed) {
        smartRefreshLayout.finishRefresh(delayed);
    }

    public void finishRefresh(final int delayed, final boolean success, final Boolean noMoreData) {
        smartRefreshLayout.finishRefresh(delayed, success, noMoreData);
    }

    public void finishLoadMore() {
        smartRefreshLayout.finishLoadMore();
    }

    public void finishLoadMore(int delayed) {
        smartRefreshLayout.finishLoadMore(delayed);
    }

    public void setNoMoreData() {
        smartRefreshLayout.setNoMoreData(true); // 提示没有更多数据
    }

    public boolean isRefreshing() {
        return smartRefreshLayout.isRefreshing();
    }

    public boolean isLoading() {
        return smartRefreshLayout.isLoading();
    }

    public void notifyDataSetChanged() {
        wrapAdapter.notifyDataSetChanged();
        stopRefreshOrLoadMore();
    }

    public void notifyItemChanged(int position) {
        if (position < 0 || position >= wrapAdapter.getItemCount()) {
            return;
        }
        wrapAdapter.notifyItemChanged(position);
        stopRefreshOrLoadMore();
    }

    public void notifyItemRangeChanged(int positionStart, int itemCount) {
        if (itemCount == 0) {
            return;
        }
        wrapAdapter.notifyItemRangeChanged(positionStart + (mHeaderView == null ? 0 : 1)
                , itemCount + (mHeaderView == null ? 0 : 1) + (mFooterView == null ? 0 : 1));
        stopRefreshOrLoadMore();
    }

    public void notifyItemRangeInserted(int positionStart, int itemCount) {
        wrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        stopRefreshOrLoadMore();
    }

    public void stopRefreshOrLoadMore() {
        stopRefreshOrLoadMore(layoutStatus);
    }

    public void stopRefreshOrLoadMore(LayoutStatus layoutStatus) {
        if (isRefreshing()) {
            finishRefresh();
        } else if (isLoading()) {
            finishLoadMore();
        }
        if (wrapAdapter.getItemCount() != 0) {
            tipLayoutView.showContent();
            if (mHeaderView != null) {
                mHeaderView.setVisibility(View.VISIBLE);
            }
            return;
        }
        // 如果没有数据就显示提示页面
        if (layoutStatus == null) {
            layoutStatus = LayoutStatus.LAYOUT_STATUS_EMPTY_REFRESH;
        }
        if (LayoutStatus.LAYOUT_STATUS_CONTENT == layoutStatus) {
            tipLayoutView.showContent();
        } else if (LayoutStatus.LAYOUT_STATUS_CONTENT_HEADER_SHOW == layoutStatus) {
            tipLayoutView.showContent();
            mHeaderView.setVisibility(View.VISIBLE);
        } else if (LayoutStatus.LAYOUT_STATUS_CONTENT_HEADER_HIDE == layoutStatus) {
            tipLayoutView.showContent();
            mHeaderView.setVisibility(View.GONE);
        } else if (LayoutStatus.LAYOUT_STATUS_EMPTY == layoutStatus) {
            tipLayoutView.showEmpty();
        } else if (LayoutStatus.LAYOUT_STATUS_EMPTY_REFRESH == layoutStatus) {
            tipLayoutView.showEmptyOrRefresh();
        } else if (LayoutStatus.LAYOUT_STATUS_EMPTY_REFRESH_TOP == layoutStatus) {
            tipLayoutView.showEmptyOrRefresh(true);
        } else if (LayoutStatus.LAYOUT_STATUS_NET_ERROR == layoutStatus) {
            tipLayoutView.showNetError();
        } else if (LayoutStatus.LAYOUT_STATUS_CUSTOM == layoutStatus) {
            tipLayoutView.showCustomError(customImage, customMessage, customBtn);
        }
    }

    public void recycleException() {
        finishRefresh();
        finishLoadMore();
        tipLayoutView.showNetError();
    }

    public void setOnRefreshListener(final OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (onRefreshListener != null) {
                    onRefreshListener.onRefresh(refreshLayout);
                }
            }
        });
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        smartRefreshLayout.setOnLoadMoreListener(onLoadMoreListener);
    }

    public void setOnReloadListener(OnReloadListener onReloadListener) {
        this.onReloadListener = onReloadListener;
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

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        recyclerView.setOnTouchListener(onTouchListener);
    }

    public void setOnItemClickListener(final ItemClickSupport.OnItemClickListener mPresenter) {
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                //之所以再用ItemClickSupport.OnItemClickListener报装一层就是为了不能点到FooterView(目前是加载更多提示)
                if (mPresenter != null) {
                    mPresenter.onItemClicked(recyclerView, position - wrapAdapter.getHeadersCount(), v);
                }
            }
        });
    }

    public void setOnItemLongClickListener(final ItemClickSupport.OnItemLongClickListener mPresenter) {
        ItemClickSupport.addTo(recyclerView).setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                //之所以再用ItemClickSupport.OnItemClickListener报装一层就是就是为了不能点到FooterView(目前是加载更多提示)
                if (mPresenter != null) {
                    return mPresenter.onItemLongClicked(recyclerView, position - wrapAdapter.getHeadersCount(), v);
                }
                return false;
            }
        });
    }

    @Override
    public void onReload() {
        tipLayoutView.showLoading();
        if (onReloadListener != null) {
            onReloadListener.onReload();
        } else {
            if (onRefreshListener != null) {
                onRefreshListener.onRefresh(smartRefreshLayout);
            }
        }
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
