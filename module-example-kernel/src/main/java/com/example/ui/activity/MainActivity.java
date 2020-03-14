package com.example.ui.activity;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.databinding.ActivityMainBinding;
import com.example.ui.base.BaseDaggerActivity;
import com.example.ui.contract.MainContract;
import com.example.ui.presenter.MainPresenter;
import com.yiciyuan.apt.annotation.Router;
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