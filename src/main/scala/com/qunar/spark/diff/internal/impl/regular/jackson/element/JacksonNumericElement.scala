package com.qunar.spark.diff.internal.impl.regular.jackson.element

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.NumericNode
import com.qunar.spark.diff.base.GenericNumber
import com.qunar.spark.diff.base.regular.elements.NumericElement

/**
  * 适用于Jackson的NumericElement
  */
private[jackson] final class JacksonNumericElement(@volatile private var interValue: GenericNumber,
                                                   private val name: String) extends NumericElement(interValue) {

  override def getName: String = name

}

private[jackson] object JacksonNumericElement {

  def apply(jsonNode: JsonNode, name: String): JacksonNumericElement = {
    jsonNode match {
      case jsonNode: NumericNode =>
        val value = GenericNumber(getActualValue(jsonNode))
        new JacksonNumericElement(value, name)
      case _ =>
        throw new IllegalArgumentException(
          "the jsonNode param does not confirm to the correct type NumericNode which JacksonNumericElement needs")
    }
  }

  /**
    * 由传入的NumericNode解析出真实的数值类型
    */
  private def getActualValue(jsonNode: NumericNode): Number = {
    val numberType: JsonParser.NumberType = jsonNode.numberType()
    numberType match {
      case JsonParser.NumberType.INT => jsonNode.intValue()
      case JsonParser.NumberType.DOUBLE => jsonNode.doubleValue()
      case JsonParser.NumberType.FLOAT => jsonNode.floatValue()
      case JsonParser.NumberType.LONG => jsonNode.longValue()
    }
  }

}
