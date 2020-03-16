package com.example.kernel.ui.core.view;

import android.view.View;

import com.example.R;
import com.example.kernel.ui.core.fragment.MvpCoreExampleTestFragment;
import com.example.utils.FragmentUtil;
import com.yiciyuan.core.ui.base.ViewImpl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created with Android Studio.
 * User: kcj
 * Date: 2020-03-07 17:55
 * Description:
 */
public class MvpCoreExampleView extends ViewImpl {

    private View[] mTabs;
    private Fragment[] mFragments;
    private MvpCoreExampleTestFragment mHomeFragment;
    private MvpCoreExampleTestFragment mConversationFragment;
    private MvpCoreExampleTestFragment mContactFragment;
    private MvpCoreExampleTestFragment mCaseManageFragment;
    private MvpCoreExampleTestFragment mUserFragment;

    private int index;
    private int currentTabIndex;

    @Override
    public int getLayoutId() {
        return R.layout.activity_mvp_core_example;
    }

    @Override
    public void created() {
        super.created();
        mTabs = new View[5];
        mTabs[0] = findViewById(R.id.btn_main_home);
        mTabs[1] = findViewById(R.id.btn_main_msg);
        mTabs[2] = findViewById(R.id.btn_main_contact);
        mTabs[3] = findViewById(R.id.btn_main_work);
        mTabs[4] = findViewById(R.id.btn_main_mine);
        mTabs[0].setSelected(true);

        mHomeFragment = MvpCoreExampleTestFragment.newInstance();
        mConversationFragment = MvpCoreExampleTestFragment.newInstance();
        mContactFragment = MvpCoreExampleTestFragment.newInstance();
        mCaseManageFragment = MvpCoreExampleTestFragment.newInstance();
        mUserFragment = MvpCoreExampleTestFragment.newInstance();
        mFragments = new Fragment[]{mHomeFragment, mConversationFragment, mContactFragment, mCaseManageFragment, mUserFragment};

        FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
        FragmentUtil.clearFragment(fragmentManager);
        fragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, mFragments[0])
                .add(R.id.fragmentContainer, mFragments[1])
//            .add(R.id.fragmentContainer, mFragments[2])
//            .add(R.id.fragmentContainer, mFragments[4])
//            .hide(mFragments[4])
//            .hide(mFragments[2])
                .hide(mFragments[1])
                .show(mFragments[0])
                .commitAllowingStateLoss();
    }

    @Override
    public void bindEvent() {
        super.bindEvent();
    }

    public void onTabSelect(View view) {
        switch (view.getId()) {
            case R.id.btn_main_home:
                index = 0;
                break;
            case R.id.btn_main_msg:
                index = 1;
                break;
            case R.id.btn_main_contact:
                index = 2;
                break;
            case R.id.btn_main_work:
                index = 3;
                break;
            case R.id.btn_main_mine:
                index = 4;
                break;
        }
        onTabSelect(index);
    }

    public void onTabSelect(int index) {
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
}
