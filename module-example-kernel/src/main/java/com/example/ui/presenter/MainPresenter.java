package com.example.ui.presenter;

import com.example.ui.contract.MainContract;
import com.yiciyuan.kernel.utils.LogUtil;

/**
 * Created with Android Studio.
 * User kcj
 * Date 2020/03/11
 * Description: 自动生成
 */
public class MainPresenter implements MainContract.Presenter{

    private MainContract.View view;

    public MainPresenter() {
    }

    @Override
    public void take(MainContract.View view) {
        this.view = view;
    }

    @Override
    public void destroy(MainContract.View view) {

    }

    @Override
    public void test() {
        LogUtil.e("MainPresenter = test");
        view.testSucceed();
    }
}
