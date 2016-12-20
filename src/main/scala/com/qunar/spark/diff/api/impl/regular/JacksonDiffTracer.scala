package com.qunar.spark.diff.api.impl.regular

import com.qunar.spark.diff.api.DiffTracer

/**
  * 适用于Jackson的incr-diff实现类
  */
class JacksonDiffTracer[T] extends DiffTracer[T] {

  override def isDifferent(target1: T, target2: T): Boolean = {
    false
  }

}
