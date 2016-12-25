package com.qunar.spark.diff.api.java;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.qunar.spark.diff.api.scala.DiffTracer;
import com.qunar.spark.diff.api.scala.DiffTracer$;
import scala.reflect.ClassTag;

/**
 * java api: 适用于Java语言的DifferTracer
 */
public final class JavaDiffTracer<T> {

    private DiffTracer<T> innerDiffTracer;

    /**
     * 构造器私有化,控制内部行为
     */
    private JavaDiffTracer(DiffTracer<T> diffTracer) {
        this.innerDiffTracer = diffTracer;
    }

    public boolean isDifferent(T target1, T target2) {
        return innerDiffTracer.isDifferent(target1, target2);
    }

    /**
     * 默认的DiffTracer实现:JacksonDiffTracer
     */
    public static <T> JavaDiffTracer<T> of() {
        DiffTracer<T> defaultDiffTracer = ScalaConverterDriver.defaultDiffTracer();
        return new JavaDiffTracer<>(defaultDiffTracer);
    }

    /**
     * 当传入两个com.fasterxml.jackson.databind.JsonNode,
     * 默认创建JacksonDiffTracer实例并调用其isDifferent方法
     *
     * @see com.fasterxml.jackson.databind.JsonNode
     */
    public static boolean compare(JsonNode target1, JsonNode target2) {
        return false;
    }

    private static final class ScalaConverterDriver {

        private static <T> DiffTracer<T> defaultDiffTracer() {
            ClassTag<T> tag = scala.reflect.ClassTag$.MODULE$.apply(getT());
            return DiffTracer$.MODULE$.apply(tag);
        }

        @SuppressWarnings("unchecked")
        private static <T> Class<T> getT() {
            return (Class<T>) new TypeReference<T>() {
            }.getType();
        }

    }

}
