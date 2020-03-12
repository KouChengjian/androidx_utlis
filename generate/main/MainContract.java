package com.example.ui.activity.main;


import dagger.Binds;
import dagger.Module;

/**
 * Created with Android Studio.
 * User kcj
 * Date 2020/03/11
 * Description: 自动生成
 */
public class MainContract {

    public interface View extends BaseView {

    }

    public interface Presenter extends BasePresenter<View> {

    }

    @Module
    public abstract class MainModule implements BaseModule{

        @PerActivity
        @Binds
        abstract Presenter presenter(MainPresenter presenter);
    }
}
