package com.qunar.spark.diff.api.java;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.qunar.spark.diff.api.scala.DiffTracer;
import com.qunar.spark.diff.api.scala.DiffTracer$;
import com.qunar.spark.diff.internal.impl.regular.jackson.JacksonDiffTracer;
import scala.reflect.ClassTag;

/**
 * java api: 适用于Java语言的DifferTracer
 */
public final class JavaDiffTracer<T> {

    /**
     * 内部维护一个DiffTracer实例
     */
    private DiffTracer<T> innerDiffTracer;

    /**
     * 构造器私有化,控制内部行为
     */
    private JavaDiffTracer(DiffTracer<T> diffTracer) {
        this.innerDiffTracer = diffTracer;
    }

    /**
     * 默认的DiffTracer实现:JacksonDiffTracer
     */
    public static <T> JavaDiffTracer<T> of() {
        DiffTracer<T> defaultDiffTracer = ScalaConverterDriver.defaultDiffTracer();
        return new JavaDiffTracer<>(defaultDiffTracer);
    }

    /**
     * 比较两个实体 targetLeft 与 targetRight 是否是不同的
     * 这里两个实体将被当作Plain Ordinary Java Object
     *
     * @param targetLeft  第一个待比较对象
     * @param targetRight 第二个待比较对象
     * @return targetLeft 与 targetRight 是否是相同的
     * <p>
     * NOTICE: 此方法开启了注解增强功能,可在T所对应Class的字段上使用以下包内的注解:
     * @see com.qunar.spark.diff.api.annotation
     */
    public boolean isDifferent(T targetLeft, T targetRight) {
        return innerDiffTracer.isDifferent(targetLeft, targetRight);
    }

    /**
     * 当传入两个{@link com.fasterxml.jackson.databind.JsonNode}时,
     * 默认创建{@link JacksonDiffTracer}实例并调用其{@link JacksonDiffTracer#isDifferent}方法
     *
     * @see com.fasterxml.jackson.databind.JsonNode
     */
    public static boolean isDifferent(JsonNode targetLeft, JsonNode targetRight) {
        return false;
    }

    /**
     * scala-java转换的驱动封装,隐藏复杂的scala api调用
     */
    private static final class ScalaConverterDriver {

        private static <T> DiffTracer<T> defaultDiffTracer() {
            ClassTag<T> tag = scala.reflect.ClassTag$.MODULE$.apply(getClassOfT());
            return DiffTracer$.MODULE$.apply(tag);
        }

        @SuppressWarnings("unchecked")
        private static <T> Class<T> getClassOfT() {
            return (Class<T>) new TypeReference<T>() {
            }.getType();
        }

    }

}
