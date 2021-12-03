package com.yiciyuan.apt.annotation;


import com.yiciyuan.apt.enums.ApiRequestType;
import com.yiciyuan.apt.enums.ApiResponseType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with Android Studio.
 * User: kcj
 * Date: 2018/8/24 10:00
 * Description: 主要是用在post json传参时，@body不想为对象时，可以使用RequestBody，创建公共map进行组装json
 * 1.CONSTRUCTOR:用于描述构造器
 * 　　　　2.FIELD:用于描述域
 * 　　　　3.LOCAL_VARIABLE:用于描述局部变量
 * 　　　　4.METHOD:用于描述方法
 * 　　　　5.PACKAGE:用于描述包
 * 　　　　6.PARAMETER:用于描述参数
 * 　　　　7.TYPE:用于描述类、接口(包括注解类型) 或enum声明
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface ApiParams {
    String[] paramName() default {};

    Class<?>[] paramType() default {};

    /**
     * Single<HttpResult> 此参数无效
     */
    ApiRequestType request() default ApiRequestType.FORMDATA;

    /**
     * Single<HttpResult> 此参数无效  Single<ResponseBody> 有效
     */
    ApiResponseType response() default ApiResponseType.NONE;
}
