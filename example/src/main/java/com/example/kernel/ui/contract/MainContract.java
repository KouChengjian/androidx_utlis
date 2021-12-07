package com.example.kernel.ui.contract;


import com.example.kernel.ui.presenter.MainPresenter;
import com.yiciyuan.kernel.ui.base.BaseModule;
import com.yiciyuan.kernel.ui.base.BasePresenter;
import com.yiciyuan.kernel.ui.base.BaseView;

/**
 * Created with Android Studio.
 * User kcj
 * Date 2020/03/11
 * Description: 自动生成
 */
public class MainContract {

    public interface View extends BaseView {
        void testSucceed();
    }

    public interface Presenter extends BasePresenter<View> {
        void test();
    }

    public abstract class MainModule implements BaseModule {

        abstract Presenter presenter(MainPresenter presenter);
    }
}
