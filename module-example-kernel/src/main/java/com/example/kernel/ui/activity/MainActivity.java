package com.example.kernel.ui.activity;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.kernel.databinding.ActivityMainBinding;
import com.example.kernel.ui.base.BaseDaggerActivity;
import com.example.kernel.ui.contract.MainContract;
import com.example.kernel.ui.presenter.MainPresenter;
import com.yiciyuan.annotation.apt.Router;
import com.yiciyuan.kernel.utils.LogUtil;

/**
 * Created with Android Studio.
 * User kcj
 * Date 2020/03/11
 * Description: 自动生成
 */
@Router("xxxx")
public class MainActivity extends BaseDaggerActivity<MainPresenter, ActivityMainBinding> implements MainContract.View {

    @Override
    protected View getLayoutView() {
        viewBinding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        return viewBinding.getRoot();
    }

    @Override
    protected void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
    }

    @Override
    protected void created(Bundle savedInstanceState) {
        super.created(savedInstanceState);
        viewBinding.btnRecycler.setText("sdfkiljasjdlaskj");
        mPresenter.test();
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
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