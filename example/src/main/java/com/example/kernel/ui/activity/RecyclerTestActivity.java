package com.example.kernel.ui.activity;


import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.kernel.R;
import com.example.kernel.databinding.ActivityRecyclerBinding;
import com.example.kernel.databinding.IncludeExampleHeaderBinding;
import com.example.kernel.ui.adapter.RecyclerTestAdapter;
import com.example.kernel.ui.base.BaseDaggerActivity;
import com.example.kernel.ui.contract.RecyclerTestContract;
import com.example.kernel.ui.presenter.RecyclerTestPresenter;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.yiciyuan.apt.annotation.Router;
import com.yiciyuan.kernel.widget.recycle.ItemClickSupport;
import com.yiciyuan.kernel.widget.recycle.listener.OnReloadListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

@Router("recyclerTest")
public class RecyclerTestActivity extends BaseDaggerActivity<RecyclerTestPresenter, ActivityRecyclerBinding> implements RecyclerTestContract.View {

    //    private int pager = 1;
    IncludeExampleHeaderBinding includeExampleHeaderBinding;
    RecyclerTestAdapter recyclerAdapter;

    @Override
    protected View getLayoutView() {
        viewBinding = ActivityRecyclerBinding.inflate(getInflater());
        return viewBinding.getRoot();
    }

    @Override
    protected void created(Bundle savedInstanceState) {
        super.created(savedInstanceState);
        viewBinding.toolBarView.setTitleOrBreak("recycle");
        viewBinding.toolBarView.setToolbarRightMenus(R.menu.menu_main);
        viewBinding.toolBarView.setNavigationOnClickListener(v -> {
            finish();
        });
        viewBinding.toolBarView.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });

        includeExampleHeaderBinding = IncludeExampleHeaderBinding.inflate(getInflater());
        View view = includeExampleHeaderBinding.getRoot();
        recyclerAdapter = new RecyclerTestAdapter(this);
        viewBinding.refreshRecyclerView.addHeader(view);
        viewBinding.refreshRecyclerView.initLinearLayoutManager();
        viewBinding.refreshRecyclerView.setAdapter(recyclerAdapter);
        viewBinding.refreshRecyclerView.autoRefresh();

        viewBinding.refreshRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                requestData();

            }
        });
        viewBinding.refreshRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                requestData();
            }
        });
        viewBinding.refreshRecyclerView.setOnReloadListener(new OnReloadListener() {
            @Override
            public void onReload() {
                requestData();
            }
        });
        viewBinding.refreshRecyclerView.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Log.e("onItemClicked", "position = " + position);
            }
        });

        requestData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void requestData() {
        List<String> list = new ArrayList<>();
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        if (viewBinding.refreshRecyclerView.isRefreshing()) {
            recyclerAdapter.setList(list);
        } else if (viewBinding.refreshRecyclerView.isLoading()) {
            recyclerAdapter.addAll(list);
        } else {
            recyclerAdapter.setList(list);
        }

        viewBinding.refreshRecyclerView.notifyDataSetChanged();
//        viewBinding.refreshRecyclerView.recycleException();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
