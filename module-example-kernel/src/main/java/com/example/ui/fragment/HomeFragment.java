package com.example.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.R;
import com.example.databinding.ActivityMainBinding;
import com.example.databinding.FragmentHomeBinding;
import com.example.ui.base.BaseDaggerFragment;
import com.example.ui.contract.HomeContract;
import com.example.ui.presenter.HomePresenter;
import com.yiciyuan.kernel.ui.fragment.BaseMvpFragment;


/**
 * Created with Android Studio.
 * User kcj
 * Date 2020/03/14
 * Description: 自动生成
 */
public class HomeFragment extends BaseDaggerFragment<HomePresenter, FragmentHomeBinding> implements HomeContract.View {

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    protected View getLayoutView() {
        viewBinding = FragmentHomeBinding.inflate(LayoutInflater.from(this));
        return viewBinding.getRoot();
    }

    @Override
    protected void created(Bundle savedInstanceState) {
        super.created(savedInstanceState);
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
