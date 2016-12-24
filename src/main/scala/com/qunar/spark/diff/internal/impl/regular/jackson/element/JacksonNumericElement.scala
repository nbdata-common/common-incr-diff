package com.qunar.spark.diff.internal.impl.regular.jackson.element

import com.fasterxml.jackson.databind.node.ValueNode
import com.qunar.spark.diff.base.GenericNumber
import com.qunar.spark.diff.base.regular.elements.NumericElement

/**
  * 适用于Jackson的NumericElement
  */
final class JacksonNumericElement(private val interValue: GenericNumber,
                                  private val jsonNode: ValueNode) extends NumericElement(interValue) {

  def this(jsonNode: ValueNode) = this(GenericNumber(jsonNode.longValue()), jsonNode)

  override def name: String = ""

}
