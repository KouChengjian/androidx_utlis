package com.example.kernel.ui.contract;


import com.example.kernel.ui.presenter.TiplayoutPresenter;
import com.yiciyuan.kernel.ui.base.BaseModule;
import com.yiciyuan.kernel.ui.base.BasePresenter;
import com.yiciyuan.kernel.ui.base.BaseView;


/**
 * Created with Android Studio.
 * User kcj
 * Date 2020/03/28
 * Description: 自动生成
 */
public class TiplayoutContract {

    public interface View extends BaseView {

    }

    public interface Presenter extends BasePresenter<View> {

    }

    public abstract class TiplayoutModule implements BaseModule {

        abstract Presenter presenter(TiplayoutPresenter presenter);
    }
}
