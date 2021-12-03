package com.yiciyuan.apt.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Created by baixiaokang on 16/12/30.
 */
@Retention(CLASS)
@Target(FIELD)
public @interface Extra {
    String value();
}
