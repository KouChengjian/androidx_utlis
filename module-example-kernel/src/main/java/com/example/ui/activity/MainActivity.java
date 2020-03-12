package com.example.ui.activity;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.databinding.ActivityMainBinding;
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
public class MainActivity extends BaseMvpActivity<MainPresenter, ActivityMainBinding> implements MainContract.View {

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
//        activityMainBinding
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