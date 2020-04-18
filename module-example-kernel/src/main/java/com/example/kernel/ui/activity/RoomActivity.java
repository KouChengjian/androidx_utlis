package com.example.kernel.ui.activity;


import android.os.Bundle;
import android.view.View;

import com.example.kernel.app.AppApplication;
import com.example.kernel.cache.AppDatabase;
import com.example.kernel.cache.dao.UserDao;
import com.example.kernel.databinding.ActivityRoomBinding;
import com.example.kernel.entity.po.UserEntity;
import com.example.kernel.ui.base.BaseDaggerActivity;
import com.example.kernel.ui.contract.RoomContract;
import com.example.kernel.ui.presenter.RoomPresenter;
import com.yiciyuan.kernel.utils.LogUtil;

import java.util.List;

import androidx.room.Room;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created with Android Studio.
 * User kcj
 * Date 2020/04/18
 * Description: 自动生成
 */
public class RoomActivity extends BaseDaggerActivity<RoomPresenter, ActivityRoomBinding> implements RoomContract.View {

    UserEntity userEntity;
    private AppDatabase mAppDatabase;

    @Override
    protected View getLayoutView() {
        viewBinding = ActivityRoomBinding.inflate(getInflater());
        return viewBinding.getRoot();
    }

    @Override
    protected void created(Bundle savedInstanceState) {
        super.created(savedInstanceState);

        mAppDatabase = Room.databaseBuilder(this, AppDatabase.class, "android_room_dev.db")
                .allowMainThreadQueries()
//                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .build();


        userEntity = new UserEntity();
        userEntity.firstName = "xxxxxxxx";
        userEntity.lastName = "qqqqqqqqq";
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        viewBinding.btnInsert.setOnClickListener(v -> {
//            mAppDatabase.userDao().insertAll(userEntity);
//            mAppDatabase.userDao()
//                    .insertAll(userEntity)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer<Integer>() {
//                        @Override
//                        public void accept(Integer entities) {
//                            LogUtil.e(entities+"================");
//                        }
//                    });
        });
        viewBinding.btnDelete.setOnClickListener(v -> {
            mAppDatabase.userDao().delete(userEntity);
        });
        viewBinding.btnQueryAll.setOnClickListener(v -> {

            List<UserEntity> userEntities =  mAppDatabase.userDao().getAll();
            LogUtil.e(userEntities.toString());
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