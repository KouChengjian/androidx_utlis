package com.example.kernel.ui.activity;


import android.os.Bundle;
import android.view.View;

import com.example.kernel.R;
import com.example.kernel.databinding.ActivityMainBinding;
import com.example.kernel.ui.base.BaseDaggerActivity;
import com.example.kernel.ui.contract.MainContract;
import com.example.kernel.ui.fragment.HomeFragment;
import com.example.kernel.ui.fragment.MsgFragment;
import com.example.kernel.ui.fragment.UserFragment;
import com.example.kernel.ui.presenter.MainPresenter;
import com.example.kernel.utlis.FragmentUtil;
import com.yiciyuan.annotation.apt.Router;
import com.yiciyuan.kernel.utils.LogUtil;
import com.yiciyuan.kernel.utils.PresenterHelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created with Android Studio.
 * User kcj
 * Date 2020/03/11
 * Description: 自动生成
 */
@Router("main")
public class MainActivity extends BaseDaggerActivity<MainPresenter, ActivityMainBinding> implements MainContract.View {

    private View[] mTabs;
    private Fragment[] mFragments;
    private HomeFragment homeFragment;
    private MsgFragment msgFragment;
    private UserFragment userFragment;

    private int currentTabIndex;

    @Override
    protected View getLayoutView() {
        viewBinding = ActivityMainBinding.inflate(getInflater());
        return viewBinding.getRoot();
    }

    @Override
    protected void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
    }

    @Override
    protected void created(Bundle savedInstanceState) {
        super.created(savedInstanceState);

        mTabs = new View[]{
                viewBinding.mainTabs.btnMainHome, viewBinding.mainTabs.btnMainMsg, viewBinding.mainTabs.btnMainMine
        };
        mTabs[0].setSelected(true);

        homeFragment = HomeFragment.newInstance();
        msgFragment = MsgFragment.newInstance();
        userFragment = UserFragment.newInstance();
        mFragments = new Fragment[]{homeFragment, msgFragment, userFragment};

        FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
        FragmentUtil.clearFragment(fragmentManager);
        fragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, mFragments[0])
                .add(R.id.fragmentContainer, mFragments[1])
                .hide(mFragments[1])
                .show(mFragments[0])
                .commitAllowingStateLoss();


        mPresenter.test();
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        PresenterHelper.click(this, viewBinding.mainTabs.btnMainHome, viewBinding.mainTabs.btnMainMsg, viewBinding.mainTabs.btnMainMine);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_main_home) {
            onTabSelect(0);
        } else if (v.getId() == R.id.btn_main_msg) {
            onTabSelect(1);
        } else if (v.getId() == R.id.btn_main_mine) {
            onTabSelect(2);
        }
    }

    private void onTabSelect(int index) {
        if (currentTabIndex != index) {
            FragmentTransaction trx = ((AppCompatActivity) getContext()).getSupportFragmentManager().beginTransaction();
            trx.hide(mFragments[currentTabIndex]);
            if (!mFragments[index].isAdded()) {
                trx.add(R.id.fragmentContainer, mFragments[index]);
            }
            //不能用commit，因为这个tabSelect不能保证在onStop（onSaveInstanceState已被屏蔽）前运行，导致崩溃
            trx.show(mFragments[index]).commitAllowingStateLoss();
        }
        mTabs[currentTabIndex].setSelected(false);
        mTabs[index].setSelected(true);
        currentTabIndex = index;
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