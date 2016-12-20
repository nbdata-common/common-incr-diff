package com.qunar.spark.diff.impl.regular

import com.fasterxml.jackson.databind.JsonNode
import com.qunar.spark.diff.api.scala.DiffTracer

/**
  * 适用于Jackson的incr-diff实现类
  */
private[diff] class JacksonDiffTracer[T] extends DiffTracer[T] {

  override def isDifferent(target1: T, target2: T): Boolean = {
    false
  }

  def isDifferent(target1: JsonNode, target2: JsonNode): Boolean = {
    false
  }

}
