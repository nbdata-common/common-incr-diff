package com.qunar.spark.diff.api.scala

import com.fasterxml.jackson.databind.JsonNode
import com.qunar.spark.diff.internal.impl.regular.jackson.JacksonDiffTracer

/**
  * incr-diff的泛化抽象
  */
trait DiffTracer[T] extends Serializable {

  /**
    * 比较两个实体(target1,target2)是否是不同的
    * 这里两个实体将被当作Plain Ordinary Java Object
    */
  def isDifferent(target1: T, target2: T): Boolean

}

/**
  * scala api: DifferTracer工厂
  */
object DiffTracer {

  /**
    * 默认的diff实现:JacksonDiffTracer
    */
  def apply[T](): DiffTracer[T] = new JacksonDiffTracer[T]()

  /**
    * 当传入两个com.fasterxml.jackson.databind.JsonNode,
    * 默认创建JacksonDiffTracer实例并调用其isDifferent方法
    *
    * @see com.fasterxml.jackson.databind.JsonNode
    */
  def apply(target1: JsonNode, target2: JsonNode): Boolean = new JacksonDiffTracer[Any].isDifferent(target1, target2)

}
