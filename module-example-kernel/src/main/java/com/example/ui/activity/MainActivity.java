package com.example.ui.activity;


import android.os.Bundle;

import com.example.R;
import com.example.ui.base.BaseMvpActivity;
import com.example.ui.contract.MainContract;
import com.example.ui.presenter.MainPresenter;
import com.yiciyuan.kernel.utils.LogUtil;

/**
 * Created with Android Studio.
 * User kcj
 * Date 2020/03/11
 * Description: 自动生成
 */
public class MainActivity extends BaseMvpActivity<MainPresenter> implements MainContract.View {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void created(Bundle savedInstanceState) {
        super.created(savedInstanceState);
        mPresenter.test();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void testSucceed() {
        LogUtil.e("hahahah");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}