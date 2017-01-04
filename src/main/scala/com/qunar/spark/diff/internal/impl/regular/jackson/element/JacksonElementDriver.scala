package com.qunar.spark.diff.internal.impl.regular.jackson.element

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node._
import com.qunar.spark.diff.base.ReAssignableArrayBuffer
import com.qunar.spark.diff.base.regular.elements.unit.UnitElement
import com.qunar.spark.diff.base.regular.elements.Element
import com.qunar.spark.diff.base.regular.elements.composite.CompositeElement

/**
  * 让Jackson [[JsonNode]]适配[[Element]]的驱动接口
  *
  * [[JsonNode]]的继承结构如下:
  * * 1 -- [[JsonNode]]
  * *   1.1 -- [[BaseJsonNode]]
  * *     1.1.1 -- [[ContainerNode]]
  * *       1.1.1.1 -- [[ArrayNode]]
  * *       1.1.1.2 -- [[ObjectNode]]
  * *     1.1.2 -- [[ValueNode]]
  * *       1.1.2.1 -- [[BooleanNode]]
  * *       1.1.2.2 -- [[TextNode]]
  * *       1.1.2.3 -- [[NumericNode]]
  * *         1.1.2.3.x -- {[[IntNode]],[[LongNode]],[[DoubleNode]],[[FloatNode]],[[ShortNode]]...}
  *
  * 我们将会以JsonNode的继承结构为基础,实现从[[JsonNode]]到[[com.qunar.spark.diff.base.regular.elements.Element]]的映射.
  */
private[jackson] object JacksonElementDriver {

  /**
    * 用于[[childrenNodesWithName]]存储[[JsonNode]]数据
    * NOTICE: 此组件是线程不安全的
    */
  private val jsonNodesBuffer = ReAssignableArrayBuffer[(String, JsonNode)](32)

  /**
    * 列举出给定[[JsonNode]]下的所有子[[JsonNode]]
    */
  def childrenNodesWithName(jsonNode: JsonNode): Iterable[(String, JsonNode)] = {
    jsonNodesBuffer.reset()
    jsonNode match {
      case jsonNode: ObjectNode =>
        val jsonNodesEntryIter = jsonNode.fields()
        while (jsonNodesEntryIter.hasNext) {
          val entry = jsonNodesEntryIter.next()
          jsonNodesBuffer += ((entry.getKey, entry.getValue))
        }

      case jsonNode: ArrayNode =>
        var count = 0
        val jsonNodesEntryIter = jsonNode.elements()
        while (jsonNodesEntryIter.hasNext) {
          val entry = jsonNodesEntryIter.next()
          jsonNodesBuffer += ((count.toString, entry))
          count += 1
        }

      case _ =>
        throw new IllegalArgumentException(
          "the jsonNode param does not confirm to the correct type ContainerNode.")
    }

    jsonNodesBuffer
  }

  /**
    * 创建一个普通元素
    */
  def makeElement(jsonNode: JsonNode, name: String): Element = {
    jsonNode match {
      case jsonNode: ContainerNode[_] => toCompositeElement(jsonNode, name)
      case jsonNode: ValueNode => toUnitElement(jsonNode, name)
      case _ => throw new IllegalArgumentException(
        "the jsonNode param does not confirm to the correct type BooleanNode which JacksonBooleanElement needs")
    }
  }

  /**
    * 创建一个根元素
    */
  def makeElement(jsonNode: JsonNode): Element = {
    //不指定name,默认是根元素
    val defaultName = "root"
    jsonNode match {
      case jsonNode: ContainerNode[_] => makeElement(jsonNode, defaultName)
      // 如果默认的根元素是ValueNode,则拦截目标Element并将其转为CompositeElement
      case jsonNode: ValueNode =>
        // 唯一的孩子Element
        val onlyChild = makeElement(jsonNode, "onlyChild")
        JacksonCompositeElement(Seq(onlyChild), defaultName)
      case _ =>
        throw new IllegalArgumentException(
          "the jsonNode param does not confirm to the correct type BooleanNode which JacksonBooleanElement needs")
    }
  }

  /**
    * 转换成[[CompositeElement]]
    */
  def toCompositeElement(jsonNode: JsonNode, name: String): CompositeElement = {
    jsonNode match {
      case jsonNode: ObjectNode => JacksonCompositeElement(name)
      case jsonNode: ArrayNode => JacksonArrayElement(name)
      case _ => throw new IllegalArgumentException(
        "the jsonNode param does not confirm to the correct type BooleanNode which JacksonBooleanElement needs")
    }
  }

  /**
    * 转换成[[UnitElement]]
    */
  def toUnitElement(jsonNode: JsonNode, name: String): UnitElement[_] = {
    jsonNode match {
      case jsonNode: TextNode => JacksonTextElement(jsonNode, name)
      case jsonNode: NumericNode => JacksonNumericElement(jsonNode, name)
      case jsonNode: BooleanNode => JacksonBooleanElement(jsonNode, name)
      case _ => throw new IllegalArgumentException(
        "the jsonNode param does not confirm to the correct type BooleanNode which JacksonBooleanElement needs")
    }
  }

}
