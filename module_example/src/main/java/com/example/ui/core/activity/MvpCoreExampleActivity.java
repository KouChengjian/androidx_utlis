package com.example.ui.core.activity;

import android.os.Bundle;
import android.view.View;

import com.example.R;
import com.example.ui.core.view.MvpCoreExampleView;
import com.yiciyuan.core.ui.activity.PresenterActivityImpl;

public class MvpCoreExampleActivity extends PresenterActivityImpl<MvpCoreExampleView> {

    @Override
    public void created(Bundle savedInstance) {
        super.created(savedInstance);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onTabSelect(View view) {
        mView.onTabSelect(view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
