package com.example.test.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.test.R;
import com.example.test.ui.adapter.RecyclerAdapter;
import com.example.test.widget.recycle.ItemClickSupport;
import com.example.test.widget.recycle.RefreshRecyclerView;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerActivity extends AppCompatActivity {

    private int pager = 1;
    RefreshRecyclerView refreshRecyclerView;
    RecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        refreshRecyclerView = findViewById(R.id.refreshRecyclerView);

        View view = LayoutInflater.from(this).inflate(R.layout.include_example_header, null);
        refreshRecyclerView.addHeader(view);
        recyclerAdapter = new RecyclerAdapter(this);
        refreshRecyclerView.initLinearLayoutManager();
        refreshRecyclerView.setAdapter(recyclerAdapter);
        refreshRecyclerView.autoRefresh();

        refreshRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                requestData();

            }
        });
        refreshRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                requestData();
            }
        });

        refreshRecyclerView.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Log.e("onItemClicked", "position = " + position);
            }
        });

        requestData();
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
        if (refreshRecyclerView.isRefreshing()) {
            recyclerAdapter.setList(list);
        } else if (refreshRecyclerView.isLoading()) {
            recyclerAdapter.addAll(list);
        } else {
            recyclerAdapter.setList(list);
        }

        refreshRecyclerView.notifyDataSetChanged();
    }
}