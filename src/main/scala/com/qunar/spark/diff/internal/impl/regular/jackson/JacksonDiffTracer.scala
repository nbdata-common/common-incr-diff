package com.qunar.spark.diff.internal.impl.regular.jackson

import com.fasterxml.jackson.databind.JsonNode
import com.qunar.spark.diff.base.compare.regular.Differ
import com.qunar.spark.diff.base.regular.elements.Element
import com.qunar.spark.diff.base.sort.Sorter
import com.qunar.spark.diff.internal.impl.regular.RegularDiffTracer
import com.qunar.spark.diff.internal.impl.regular.jackson.json.JsonMapper

/**
  * 适用于Jackson的incr-diff实现类
  */
private[diff] class JacksonDiffTracer[T] extends RegularDiffTracer[T] {

  override protected val differ: Differ = Differ()

  override protected val sorter: Sorter = Sorter()

  override def isDifferent(target1: T, target2: T): Boolean = {
    val node1 = JsonMapper.readTree(JsonMapper.writeValueAsString(target1))
    val node2 = JsonMapper.readTree(JsonMapper.writeValueAsString(target2))
    isDifferent(node1, node2)
  }

  def isDifferent(target1: JsonNode, target2: JsonNode): Boolean = {
    val node1 = jsonNodeToElement(target1)
    val node2 = jsonNodeToElement(target2)
    isDifferent(node1, node2)
  }

  private def jsonNodeToElement(node: JsonNode): Element = {
    null
  }

}
