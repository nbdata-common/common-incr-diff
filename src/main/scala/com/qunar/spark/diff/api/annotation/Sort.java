package com.qunar.spark.diff.api.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * [功能拓展] 对指定的数组(容器)类字段要求必须有序
 * <p/>
 * 默认情况下我们认为数组(容器)类字段是无序的,即当两个容器拥有相同数目及
 * 相同内容的元素时,我们认为两个容器是相同的,而不管元素顺序.
 * 若字段被打上该注解,则强制要求两个容器内元素的内容必须一模一样时才认为是相同的.
 * <p>
 * NOTICE:  该注解只能用于数组(容器)类字段
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface Sort {
}
