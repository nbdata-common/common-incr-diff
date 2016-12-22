package com.qunar.spark.diff.api.java;

import com.fasterxml.jackson.databind.JsonNode;
import com.qunar.spark.diff.api.scala.DiffTracer;
import com.qunar.spark.diff.internal.impl.regular.jackson.JacksonDiffTracer;

/**
 * java api: DifferTracer工厂
 */
public class DiffTracerFactory {

    /**
     * 默认的diff实现:JacksonDiffTracer
     */
    public static <T> DiffTracer<T> of() {
        return new JacksonDiffTracer<>();
    }

    /**
     * 当传入两个com.fasterxml.jackson.databind.JsonNode,
     * 默认创建JacksonDiffTracer实例并调用其isDifferent方法
     *
     * @see com.fasterxml.jackson.databind.JsonNode
     */
    public static boolean of(JsonNode target1, JsonNode target2) {
        return new JacksonDiffTracer().isDifferent(target1, target2);
    }

}
