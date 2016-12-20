package com.qunar.spark.diff.api

import com.qunar.spark.diff.api.impl.regular.JacksonDiffTracer

/**
  * incr-diff的泛化抽象
  */
private[diff] trait DiffTracer[T] extends Serializable {

  def isDifferent(target1: T, target2: T): Boolean

}

object DiffTracer {

  /**
    * 默认的diff实现:JacksonDiffTracer
    */
  def apply[T](): DiffTracer[T] = new JacksonDiffTracer[T]()

  def apply(): Boolean = {
    false
  }

}
