package com.example.kernel.utlis;


import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentUtil {

    public static void clearFragment(FragmentManager fragmentManager){
        List<Fragment> list = fragmentManager.getFragments();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (list != null && !list.isEmpty()) {
            for (Fragment f : list) {
                transaction.remove(f);
            }
        }
        transaction.commitNowAllowingStateLoss();
    }
}
