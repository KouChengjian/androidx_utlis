package com.yiciyuan.annotation.apt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface ApiConfig {

    int resultSucceed() default 200;

    String resultCode() default "resCode";

    String resultMsg() default "resMsg";

    String resultData() default "data";

    String resultListTotal() default "total";

    String resultListPages() default "pages";

    String resultListData() default "list";
}
