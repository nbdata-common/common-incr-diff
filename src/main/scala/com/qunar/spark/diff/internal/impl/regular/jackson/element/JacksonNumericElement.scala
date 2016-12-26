package com.qunar.spark.diff.internal.impl.regular.jackson.element

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.{ContainerNode, ValueNode}
import com.qunar.spark.diff.base.GenericNumber
import com.qunar.spark.diff.base.regular.elements.NumericElement

/**
  * 适用于Jackson的NumericElement
  */
private[jackson] final class JacksonNumericElement(private val interValue: GenericNumber,
                                                   private val name: String) extends NumericElement(interValue) {

  def this(jsonNode: ValueNode, name: String) = this(GenericNumber(jsonNode.longValue()), name)

  override def getName: String = name

}

private[jackson] object JacksonNumericElement {

  def apply(jsonNode: JsonNode, name: String): JacksonNumericElement = {
    jsonNode match {
      case jsonNode: ContainerNode =>

    }
  }

  private def getActualValue(jsonNode: JsonNode): Number = {

  }

}
