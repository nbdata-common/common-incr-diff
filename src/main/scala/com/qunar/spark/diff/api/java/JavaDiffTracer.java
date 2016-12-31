package com.qunar.spark.diff.api.java;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.qunar.spark.diff.api.scala.*;
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
        JacksonDiffTracer<Boolean> diffTracer = (JacksonDiffTracer<Boolean>) ScalaConverterDriver.<Boolean>defaultDiffTracer();
        return diffTracer.isDifferent(targetLeft, targetRight);
    }

    /**
     * scala-java转换的驱动封装,隐藏复杂的scala api调用
     */
    private static final class ScalaConverterDriver {

        /**
         * 默认的{@link DiffTracer}实现:{@link JacksonDiffTracer}
         */
        private static <T> DiffTracer<T> defaultDiffTracer() {
            ClassTag<T> tag = getClassTagOfT();
            return DiffTracer$.MODULE$.apply(tag);
        }

        /**
         * 使用Jackson的{@link TypeReference}获得泛型T所对应的实际类型
         */
        @SuppressWarnings("unchecked")
        private static <T> Class<T> getClassOfT() {
            return (Class<T>) new TypeReference<T>() {
            }.getType();
        }

        /**
         * 由{@link Class<T>}转换为{@link ClassTag<T>}
         */
        private static <T> ClassTag<T> getClassTagOfT() {
            return scala.reflect.ClassTag$.MODULE$.apply(getClassOfT());
        }

    }

}
