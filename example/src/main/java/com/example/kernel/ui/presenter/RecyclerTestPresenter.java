package com.example.kernel.ui.presenter;

import com.example.kernel.ui.contract.RecyclerTestContract;

/**
 * Created with Android Studio.
 * User kcj
 * Date 2020/03/23
 * Description: 自动生成
 */
public class RecyclerTestPresenter implements RecyclerTestContract.Presenter{

    private RecyclerTestContract.View view;

    public RecyclerTestPresenter() {
    }

    @Override
    public void take(RecyclerTestContract.View view) {
        this.view = view;
    }

    @Override
    public void destroy(RecyclerTestContract.View view) {

    }
}
