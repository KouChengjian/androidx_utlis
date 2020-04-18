package com.example.kernel.ui.presenter;

import com.example.kernel.ui.contract.RoomContract;

/**
 * Created with Android Studio.
 * User kcj
 * Date 2020/04/18
 * Description: 自动生成
 */
public class RoomPresenter implements RoomContract.Presenter{

    private RoomContract.View view;

    public RoomPresenter() {
    }

    @Override
    public void take(RoomContract.View view) {
        this.view = view;
    }

    @Override
    public void destroy(RoomContract.View view) {

    }
}
