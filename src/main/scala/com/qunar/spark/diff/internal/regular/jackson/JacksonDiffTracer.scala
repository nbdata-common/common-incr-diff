package com.qunar.spark.diff.internal.regular.jackson

import com.fasterxml.jackson.databind.JsonNode
import com.qunar.spark.diff.base.compare.Differ
import com.qunar.spark.diff.internal.regular.RegularDiffTracer

/**
  * 适用于Jackson的incr-diff实现类
  */
private[diff] class JacksonDiffTracer[T] extends RegularDiffTracer[T] {

  override protected val differ: Differ = Differ()

  override def isDifferent(target1: T, target2: T): Boolean = {
    false
  }

  def isDifferent(target1: JsonNode, target2: JsonNode): Boolean = {
    false
  }

}
