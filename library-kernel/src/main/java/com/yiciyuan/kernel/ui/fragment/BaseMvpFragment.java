package com.yiciyuan.kernel.ui.fragment;

import android.os.Bundle;

import com.yiciyuan.kernel.ui.base.BasePresenter;
import com.yiciyuan.kernel.utils.PresenterHelper;

import androidx.viewbinding.ViewBinding;

/**
 * Created with Android Studio.
 * UserEntity: kcj
 * Date: 2019/1/10 18:40
 * Description:
 */
public abstract class BaseMvpFragment<P extends BasePresenter, V extends ViewBinding> extends BaseFragment {

    protected P mPresenter;
    protected V viewBinding;

    @Override
    protected void create(Bundle savedInstanceState) {
        try {
            mPresenter = getPresenterClass().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    @Override
    protected void created(Bundle savedInstanceState) {
        if (mPresenter != null) {
            mPresenter.take(this);
        }
    }

    public Class<P> getPresenterClass() {
        return PresenterHelper.getViewClass(getClass());
    }

    @Override
    public void onDestroy() {
        //把所有的数据销毁掉
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.destroy(this);//释放资源
        this.mPresenter = null;
    }
}
