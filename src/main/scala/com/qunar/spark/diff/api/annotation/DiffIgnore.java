package com.qunar.spark.diff.api.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * [功能拓展] 在diff中忽略指定元素
 * <p>
 * 在指定字段上打上此注解,在diff中将忽略此字段的不同
 * 该注解没有需指定的成员
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface DiffIgnore {
}
