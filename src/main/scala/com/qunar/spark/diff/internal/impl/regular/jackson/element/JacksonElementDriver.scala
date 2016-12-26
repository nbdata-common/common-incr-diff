package com.qunar.spark.diff.internal.impl.regular.jackson.element

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node._
import com.qunar.spark.diff.base.regular.elements.{CompositeElement, Element, UnitElement}

/**
  * 让Jackson JsonNode适配Element的驱动接口
  *
  * [[JsonNode]]的继承结构如下:
  * * 1 -- JsonNode
  * *   1.1 -- BaseJsonNode
  * *     1.1.1 -- ContainerNode
  * *       1.1.1.1 -- ArrayNode
  * *       1.1.1.2 -- ObjectNode
  * *     1.1.2 -- ValueNode
  * *       1.1.2.1 -- BooleanNode
  * *       1.1.2.2 -- TextNode
  * *       1.1.2.3 -- NumericNode
  * *         1.1.2.3.x -- {IntNode,LongNode,DoubleNode,FloatNode,ShortNode ...}
  *
  * 我们将会以JsonNode的继承结构为基础,实现从[[JsonNode]]到[[com.qunar.spark.diff.base.regular.elements.Element]]的映射.
  */
private[jackson] object JacksonElementDriver {

  def toElement(jsonNode: JsonNode): Element = {
    jsonNode match {
      case jsonNode: ValueNode => toUnitElement(jsonNode)
      case jsonNode: ObjectNode => toCompositeElement(jsonNode)
      case jsonNode: ArrayNode => {
        toCompositeElement(jsonNode)
      }
    }
  }

  def toCompositeElement(jsonNode: JsonNode): CompositeElement = {
    jsonNode match {
      case jsonNode:
    }
  }

  def toUnitElement(jsonNode: JsonNode): UnitElement = {
    jsonNode match {
      case jsonNode: TextNode =>
      case jsonNode: NumericNode => JacksonNumericElement(jsonNode)
      case jsonNode: BooleanNode =>
    }
  }

}
