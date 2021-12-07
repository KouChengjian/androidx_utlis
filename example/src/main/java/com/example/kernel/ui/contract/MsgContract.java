package com.example.kernel.ui.contract;


import com.example.kernel.ui.presenter.MsgPresenter;
import com.yiciyuan.kernel.ui.base.BaseModule;
import com.yiciyuan.kernel.ui.base.BasePresenter;
import com.yiciyuan.kernel.ui.base.BaseView;


/**
 * Created with Android Studio.
 * User kcj
 * Date 2020/03/23
 * Description: 自动生成
 */
public class MsgContract {

    public interface View extends BaseView {

    }

    public interface Presenter extends BasePresenter<View> {

    }

    public abstract class MsgModule implements BaseModule {

        abstract Presenter presenter(MsgPresenter presenter);
    }
}
