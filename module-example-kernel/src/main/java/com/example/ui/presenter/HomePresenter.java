package com.example.ui.presenter;

import com.example.ui.contract.HomeContract;

/**
 * Created with Android Studio.
 * User kcj
 * Date 2020/03/14
 * Description: 自动生成
 */
public class HomePresenter implements HomeContract.Presenter{

    private HomeContract.View view;

    HomePresenter() {
    }

    @Override
    public void take(HomeContract.View view) {
        this.view = view;
    }

    @Override
    public void destroy(HomeContract.View view) {

    }
}
