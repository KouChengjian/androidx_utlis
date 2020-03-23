package com.example.kernel.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.example.kernel.R;
import com.example.kernel.databinding.FragmentUserBinding;
import com.example.kernel.ui.base.BaseDaggerFragment;
import com.example.kernel.ui.contract.UserContract;
import com.example.kernel.ui.presenter.UserPresenter;
import com.yiciyuan.kernel.ui.fragment.BaseMvpFragment;


/**
 * Created with Android Studio.
 * User kcj
 * Date 2020/03/23
 * Description: 自动生成
 */
public class UserFragment extends BaseDaggerFragment<UserPresenter, FragmentUserBinding> implements UserContract.View {

    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();
        return fragment;
    }

    @Override
    protected View getLayoutView() {
        viewBinding = FragmentUserBinding.inflate(getInflater());
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
