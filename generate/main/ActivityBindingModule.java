package com.example.ui.activity.main;


/**
 * Created with Android Studio.
 * User kcj
 * Date 2020/03/11
 * Description: 自动生成
 */
public abstract class ActivityBindingModule {

     @PerActivity
     @ContributesAndroidInjector(modules = MainContract.MainModule.class)
     abstract MainActivity MainActivity();
}