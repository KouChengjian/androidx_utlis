package com.example.kernel.ui.activity;


import android.os.Bundle;
import android.view.View;

import com.example.kernel.cache.AppDatabase;
import com.example.kernel.cache.RoomObserver;
import com.example.kernel.databinding.ActivityRoomBinding;
import com.example.kernel.entity.po.UserEntity;
import com.example.kernel.ui.base.BaseDaggerActivity;
import com.example.kernel.ui.contract.RoomContract;
import com.example.kernel.ui.presenter.RoomPresenter;
import com.google.gson.Gson;
import com.yiciyuan.kernel.utils.LogUtil;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created with Android Studio.
 * User kcj
 * Date 2020/04/18
 * Description: 自动生成
 */
public class RoomActivity extends BaseDaggerActivity<RoomPresenter, ActivityRoomBinding> implements RoomContract.View {

    UserEntity userEntity;

    @Override
    protected View getLayoutView() {
        viewBinding = ActivityRoomBinding.inflate(getInflater());
        return viewBinding.getRoot();
    }

    @Override
    protected void created(Bundle savedInstanceState) {
        super.created(savedInstanceState);

        userEntity = new UserEntity();
        userEntity.uid = 2;
        userEntity.firstName = "xxxxxxxx";
        userEntity.lastName = "qqqqqqqqq";
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        viewBinding.btnInsert.setOnClickListener(v -> {
            AppDatabase.getDatabase(getContext())
                    .userDao()
                    .insert(userEntity)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RoomObserver<Long>() {
                        @Override
                        public void onSuccess(Long aLong) {
                            LogUtil.e(aLong + "================");
                        }
                    });
        });
        viewBinding.btnUpdate.setOnClickListener(v -> {
            userEntity.firstName = "iOS大计算机啊";
            AppDatabase.getDatabase(getContext())
                    .userDao()
                    .update(userEntity)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RoomObserver<Integer>() {
                        @Override
                        public void onSuccess(Integer aLong) {
                            LogUtil.e(aLong + "================");
                        }
                    });
        });
        viewBinding.btnDelete.setOnClickListener(v -> {
            AppDatabase.getDatabase(getContext())
                    .userDao()
                    .delete(userEntity)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RoomObserver<Integer>() {
                        @Override
                        public void onSuccess(Integer aLong) {
                            LogUtil.e(aLong + "================");
                        }
                    });
        });
        viewBinding.btnQueryAll.setOnClickListener(v -> {
            AppDatabase.getDatabase(getContext())
                    .userDao()
                    .getAll()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RoomObserver<List<UserEntity>>() {
                        @Override
                        public void onSuccess(List<UserEntity> userEntities) {
                            LogUtil.e(new Gson().toJson(userEntities));
                        }
                    });
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