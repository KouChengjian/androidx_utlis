package com.example.kernel.ui.contract;


import com.example.kernel.ui.presenter.UserPresenter;
import com.yiciyuan.kernel.ui.base.BaseModule;
import com.yiciyuan.kernel.ui.base.BasePresenter;
import com.yiciyuan.kernel.ui.base.BaseView;


/**
 * Created with Android Studio.
 * User kcj
 * Date 2020/03/23
 * Description: 自动生成
 */
public class UserContract {

    public interface View extends BaseView {

    }

    public interface Presenter extends BasePresenter<View> {

    }

    public abstract class UserModule implements BaseModule {

        abstract Presenter presenter(UserPresenter presenter);
    }
}
