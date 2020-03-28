package com.example.kernel.ui.presenter;

import com.example.kernel.ui.contract.TiplayoutContract;

/**
 * Created with Android Studio.
 * User kcj
 * Date 2020/03/28
 * Description: 自动生成
 */
public class TiplayoutPresenter implements TiplayoutContract.Presenter{

    private TiplayoutContract.View view;

    public TiplayoutPresenter() {
    }

    @Override
    public void take(TiplayoutContract.View view) {
        this.view = view;
    }

    @Override
    public void destroy(TiplayoutContract.View view) {

    }
}
