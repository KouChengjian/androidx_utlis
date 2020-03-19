package com.yiciyuan.annotation.apt;

import com.yiciyuan.annotation.enums.ApiRequestType;
import com.yiciyuan.annotation.enums.ApiResponseType;

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
