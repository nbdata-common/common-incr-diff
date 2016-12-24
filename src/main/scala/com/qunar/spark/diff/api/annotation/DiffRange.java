package com.qunar.spark.diff.api.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * [功能拓展] 在diff中对指定元素允许存在给定范围内的差异
 * <p>
 * 在指定字段上打上此注解,并
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface DiffRange {


    String value();

}
