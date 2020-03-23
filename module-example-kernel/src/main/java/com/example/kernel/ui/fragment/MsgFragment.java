package com.example.kernel.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.example.kernel.R;
import com.example.kernel.databinding.FragmentMsgBinding;
import com.example.kernel.ui.base.BaseDaggerFragment;
import com.example.kernel.ui.contract.MsgContract;
import com.example.kernel.ui.presenter.MsgPresenter;
import com.yiciyuan.kernel.ui.fragment.BaseMvpFragment;


/**
 * Created with Android Studio.
 * User kcj
 * Date 2020/03/23
 * Description: 自动生成
 */
public class MsgFragment extends BaseDaggerFragment<MsgPresenter, FragmentMsgBinding> implements MsgContract.View {

    public static MsgFragment newInstance() {
        MsgFragment fragment = new MsgFragment();
        return fragment;
    }

    @Override
    protected View getLayoutView() {
        viewBinding = FragmentMsgBinding.inflate(getInflater());
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
