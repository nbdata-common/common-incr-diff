package com.qunar.spark.diff.api.impl.irregular

import com.qunar.spark.diff.api.DiffTracer


/**
  * 适用于对字符串作diff
  */
class StringDiffTracer[T] extends DiffTracer[T] {

  override def isDifferent(target1: T, target2: T): Boolean = {
    false
  }

  def isDifferent(target1: String, target2: String): Boolean = {
    false
  }

}
