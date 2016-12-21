package com.qunar.spark.diff.api.java;

/**
 * java api: 可以被diff比较的类型
 */
@SuppressWarnings("unused")
public enum ComparableType {

    JACKSON,    // com.fasterxml.jackson
    FASTJSON,   // com.alibaba.fastjson
    DOM4J,      // org.dom4j
    ;

}
