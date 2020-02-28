package com.example.test.ui.activity;

import android.os.Bundle;

import com.example.test.ui.adapter.RecyclerAdapter;
import com.example.test.widget.recycle.RefreshRecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.test.R;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;

public class RecyclerActivity extends AppCompatActivity {


    RefreshRecyclerView refreshRecyclerView;
    RecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        refreshRecyclerView = findViewById(R.id.refreshRecyclerView);


        List<String > list = new ArrayList<>();
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
        list.add("");
        list.add("");
        recyclerAdapter = new RecyclerAdapter(this);
        recyclerAdapter.setList(list);
        refreshRecyclerView.initLinearLayoutManager();
        refreshRecyclerView.setAdapter(recyclerAdapter);

        refreshRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshRecyclerView.finishRefresh(1000 , true ,false);
            }
        });
        refreshRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshRecyclerView.finishLoadMore(1000);
            }
        });
    }

}
