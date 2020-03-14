package com.yiciyuan.kernel.ui.fragment;

import android.os.Bundle;

import com.yiciyuan.kernel.ui.base.BasePresenter;
import com.yiciyuan.kernel.utils.PresenterHelper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import androidx.viewbinding.ViewBinding;

/**
 * Created with Android Studio.
 * UserEntity: kcj
 * Date: 2019/1/10 18:40
 * Description:
 */
public abstract class BaseMvpFragment<P extends BasePresenter, V extends ViewBinding> extends BaseFragment {

    protected P mPresenter;
    protected V viewBinding;

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void create(Bundle savedInstanceState) {
        try {
            mPresenter = getPresenterClass(getClass()).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    @Override
    protected void created(Bundle savedInstanceState) {
        if (mPresenter != null) {
            mPresenter.take(this);
        }
    }

    public Class<P> getPresenterClass(Class<?> klass) {
//        // getSuperclass()获得该类的父类 class com.test.Person
//        // getGenericSuperclass()获得带有泛型的父类  com.test.Person<com.test.Student>
//        // Type是 Java 编程语言中所有类型的公共高级接口。它们包括原始类型、参数化类型、数组类型、类型变量和基本类型。
//        Type type = klass.getGenericSuperclass();
////        LogUtil.e(klass.getSuperclass() + "===");
////        LogUtil.e(klass.getGenericSuperclass() + "===");
//        // 是否支持泛型
//        if (type == null || !(type instanceof ParameterizedType))
//            return null;
//        // ParameterizedType参数化类型，即泛型
//        ParameterizedType parameterizedType = (ParameterizedType) type;
//        // getActualTypeArguments获取参数化类型的数组，泛型可能有多个
//        Type[] types = parameterizedType.getActualTypeArguments();
//        if (types == null || types.length == 0)
//            return null;
//        for (int i = 0; i < types.length; i++) {
//            Class<P> s = (Class<P>) types[i];
////            LogUtil.e("i = " + i + "   " + s.getName());
//        }
//        return (Class<P>) types[0];
        return PresenterHelper.getViewClass(getClass());
    }

//    public Observable<Object> eventClick(View view) {
//        return eventClick(view, 1000);
//    }
//
//    public Observable<Object> eventClick(View view, int milliseconds) {
//        return RxView.clicks(view)
//                .throttleFirst(milliseconds, TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread());
//    }

    @Override
    public void onDestroy() {
        //把所有的数据销毁掉
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.destroy(this);//释放资源
        this.mPresenter = null;
    }
}
