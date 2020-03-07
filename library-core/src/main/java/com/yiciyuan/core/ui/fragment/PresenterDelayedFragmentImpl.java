package com.yiciyuan.core.ui.fragment;

import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;

import com.yiciyuan.core.ui.base.IView;
import com.yiciyuan.core.utils.MarkerUtil;

/**
 * Created with Android Studio.
 * User: kcj
 * Date: 2020-03-07 15:30
 * Description:
 */
public class PresenterDelayedFragmentImpl<T extends IView> extends PresenterFragmentImpl<T> {

    @Override
    public void create(Bundle savedInstance) {
        super.create(savedInstance);
        // TODO: 布局根布局{ViewRoot}
    }

    @Override
    public void created(Bundle savedInstance) {
        super.created(savedInstance);
        // TODO:目前已知公司测试机小米、华为没问题，占时就这两款手机开启
        if (MarkerUtil.isHuawei() || MarkerUtil.isXiaomi()) {
            addIdleHandler(savedInstance);
        } else {
            queueIdle(savedInstance);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public void createdDelay(Bundle savedInstance) {
    }

    private final void addIdleHandler(final Bundle savedInstanceState) {
        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                PresenterDelayedFragmentImpl.this.queueIdle(savedInstanceState);
                return false;
            }
        });
    }

    private final void queueIdle(Bundle savedInstanceState) {
        mView.createdDelay();
        mView.bindEventDelay();
        createdDelay(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

