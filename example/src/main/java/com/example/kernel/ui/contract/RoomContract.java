package com.example.kernel.ui.contract;


import com.example.kernel.ui.presenter.RoomPresenter;
import com.yiciyuan.kernel.ui.base.BaseModule;
import com.yiciyuan.kernel.ui.base.BasePresenter;
import com.yiciyuan.kernel.ui.base.BaseView;


/**
 * Created with Android Studio.
 * User kcj
 * Date 2020/04/18
 * Description: 自动生成
 */
public class RoomContract {

    public interface View extends BaseView {

    }

    public interface Presenter extends BasePresenter<View> {

    }

    public abstract class RoomModule implements BaseModule {

        abstract Presenter presenter(RoomPresenter presenter);
    }
}
