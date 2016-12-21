package com.qunar.spark.diff.api.scala

/**
  * scala api: 可以被diff比较的类型
  */
object ComparableType extends Enumeration {

  type ComparableType = Value

  val JACKSON, FASTJSON, DOM4J = Value

}
