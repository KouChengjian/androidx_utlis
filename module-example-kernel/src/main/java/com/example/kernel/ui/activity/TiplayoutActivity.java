package com.example.kernel.ui.activity;


import android.os.Bundle;
import android.view.View;

import com.example.kernel.R;
import com.example.kernel.databinding.ActivityTiplayoutBinding;
import com.example.kernel.ui.base.BaseDaggerActivity;
import com.example.kernel.ui.contract.TiplayoutContract;
import com.example.kernel.ui.presenter.TiplayoutPresenter;
import com.yiciyuan.widget.TipLayoutView;

/**
 * Created with Android Studio.
 * User kcj
 * Date 2020/03/28
 * Description: 自动生成
 */
public class TiplayoutActivity extends BaseDaggerActivity<TiplayoutPresenter, ActivityTiplayoutBinding> implements TiplayoutContract.View {

    @Override
    protected View getLayoutView() {
        viewBinding = ActivityTiplayoutBinding.inflate(getInflater());
        return viewBinding.getRoot();
    }

    @Override
    protected void created(Bundle savedInstanceState) {
        super.created(savedInstanceState);

        viewBinding.tipLayoutView.showNetError();

        viewBinding.tipLayoutView.setOnReloadClick(new TipLayoutView.OnReloadClick() {
            @Override
            public void onReload() {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}