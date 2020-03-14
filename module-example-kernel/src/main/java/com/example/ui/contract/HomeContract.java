package com.example.ui.contract;


import com.example.ui.presenter.HomePresenter;
import com.yiciyuan.kernel.ui.base.BaseModule;
import com.yiciyuan.kernel.ui.base.BasePresenter;
import com.yiciyuan.kernel.ui.base.BaseView;


/**
 * Created with Android Studio.
 * User kcj
 * Date 2020/03/14
 * Description: 自动生成
 */
public class HomeContract {

    public interface View extends BaseView {

    }

    public interface Presenter extends BasePresenter<View> {

    }

    public abstract class HomeModule implements BaseModule {

        abstract Presenter presenter(HomePresenter presenter);
    }
}
