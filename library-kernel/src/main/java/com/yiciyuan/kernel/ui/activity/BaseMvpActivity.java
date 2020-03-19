package com.yiciyuan.kernel.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.yiciyuan.kernel.ui.base.BasePresenter;
import com.yiciyuan.kernel.utils.PresenterHelper;

import androidx.viewbinding.ViewBinding;

/**
 * Created with Android Studio.
 * UserEntity: KCJ
 * Date: 2019-06-19 17:09
 * Description:
 */
public abstract class BaseMvpActivity<P extends BasePresenter, V extends ViewBinding> extends BaseActivity {

    protected P mPresenter;
    protected V viewBinding;

    @Override
    protected View getLayoutView() {
        return null;
    }

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
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.destroy(this);//释放资源
        this.mPresenter = null;
    }
}
