package com.example.kernel.ui.core.fragment;

import android.os.Bundle;

import com.example.kernel.ui.core.view.MvpCoreExampleTestView;
import com.yiciyuan.core.ui.fragment.PresenterFragmentImpl;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class MvpCoreExampleTestFragment extends PresenterFragmentImpl<MvpCoreExampleTestView> {

    public static MvpCoreExampleTestFragment newInstance() {
        MvpCoreExampleTestFragment fragment = new MvpCoreExampleTestFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
}
