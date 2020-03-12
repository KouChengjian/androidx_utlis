package com.example.ui.base;

import android.os.Bundle;

import com.yiciyuan.kernel.ui.base.BasePresenter;
import com.yiciyuan.kernel.ui.fragment.BaseDaggerFragment;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 * Created with Android Studio.
 * UserEntity: kcj
 * Date: 2019/1/10 18:40
 * Description:
 */
public abstract class BaseMvpFragment<P extends BasePresenter> extends BaseDaggerFragment {

    @Inject
    protected P mPresenter;

    @Override
    protected void create(Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
    }

    @Override
    protected void created(Bundle savedInstanceState) {
        //和View绑定
        if (mPresenter != null) {
            mPresenter.take(this);
        }
    }

//    public Observable<Object> eventClick(View view) {
//        return eventClick(view, 1000);
//    }
//
//    public Observable<Object> eventClick(View view, int milliseconds) {
//        return RxView.clicks(view)
//                .throttleFirst(milliseconds, TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread());
//    }

    @Override
    public void onDestroy() {
        //把所有的数据销毁掉
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.destroy(this);//释放资源
        this.mPresenter = null;
    }
}
