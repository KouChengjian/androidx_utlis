package com.example.kernel.ui.presenter;

import com.example.kernel.ui.contract.UserContract;

/**
 * Created with Android Studio.
 * User kcj
 * Date 2020/03/23
 * Description: 自动生成
 */
public class UserPresenter implements UserContract.Presenter{

    private UserContract.View view;

    public UserPresenter() {
    }

    @Override
    public void take(UserContract.View view) {
        this.view = view;
    }

    @Override
    public void destroy(UserContract.View view) {

    }
}
