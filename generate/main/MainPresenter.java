package com.example.ui.activity.main;

/**
 * Created with Android Studio.
 * User kcj
 * Date 2020/03/11
 * Description: 自动生成
 */
@PerActivity
public class MainPresenter implements MainContract.Presenter{

    private MainContract.View view;

    @Inject
    MainPresenter() {
    }

    @Override
    public void take(MainContract.View view) {
        this.view = view;
    }

    @Override
    public void destroy(MainContract.View view) {

    }
}
