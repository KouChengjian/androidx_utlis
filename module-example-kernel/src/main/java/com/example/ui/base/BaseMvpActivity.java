package com.example.ui.base;

import android.os.Bundle;

import com.yiciyuan.kernel.net.exception.ExceptionHandler;
import com.yiciyuan.kernel.ui.activity.BaseDaggerActivity;
import com.yiciyuan.kernel.ui.base.BasePresenter;
import com.yiciyuan.kernel.utils.LogUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.inject.Inject;

/**
 * Created with Android Studio.
 * UserEntity: KCJ
 * Date: 2019-06-19 17:09
 * Description:
 */
public abstract class BaseMvpActivity<P extends BasePresenter> extends BaseDaggerActivity {

    @Inject
    protected P mPresenter;

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
        //和View绑定
        if (mPresenter != null) {
            mPresenter.take(this);
        }
    }

    public Class<P> getPresenterClass(Class<?> klass) {
        // getSuperclass()获得该类的父类 class com.test.Person
        // getGenericSuperclass()获得带有泛型的父类  com.test.Person<com.test.Student>
        // Type是 Java 编程语言中所有类型的公共高级接口。它们包括原始类型、参数化类型、数组类型、类型变量和基本类型。
        Type type = klass.getGenericSuperclass();
//        LogUtil.e(klass.getSuperclass() + "===");
//        LogUtil.e(klass.getGenericSuperclass() + "===");
        // 是否支持泛型
        if (type == null || !(type instanceof ParameterizedType))
            return null;
        // ParameterizedType参数化类型，即泛型
        ParameterizedType parameterizedType = (ParameterizedType) type;
        // getActualTypeArguments获取参数化类型的数组，泛型可能有多个
        Type[] types = parameterizedType.getActualTypeArguments();
        if (types == null || types.length == 0)
            return null;
        for (int i = 0; i < types.length; i++) {
            Class<P> s = (Class<P>) types[i];
//            LogUtil.e("i = " + i + "   " + s.getName());
        }
        return (Class<P>) types[0];
    }

    @Override
    public boolean intercept(Throwable throwable) {
        ExceptionHandler handler = ExceptionHandler.getInstance();
        int code = handler.transToCode(throwable);
//        if (code == ResultCode.ERROR_LOGIN_OUTTIME) {
////            showMsg("登陆过期，请重新登陆。");
//            if (AccountManager.getInstance().isLogin()) {
//                AccountManager.getInstance().logout();
//            }
//            return true;
//        }
        return false;
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
    protected void onDestroy() {
        //把所有的数据销毁掉
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.destroy(this);//释放资源
        this.mPresenter = null;
    }
}
