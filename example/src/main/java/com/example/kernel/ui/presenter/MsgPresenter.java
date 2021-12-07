package com.example.kernel.ui.presenter;

import com.example.kernel.ui.contract.MsgContract;

/**
 * Created with Android Studio.
 * User kcj
 * Date 2020/03/23
 * Description: 自动生成
 */
public class MsgPresenter implements MsgContract.Presenter{

    private MsgContract.View view;

    public MsgPresenter() {
    }

    @Override
    public void take(MsgContract.View view) {
        this.view = view;
    }

    @Override
    public void destroy(MsgContract.View view) {

    }
}
