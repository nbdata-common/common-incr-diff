package com.qunar.spark.diff.internal.impl.regular.jackson.element

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.BooleanNode
import com.qunar.spark.diff.base.regular.elements.BooleanElement

/**
  * 适用于Jackson的BooleanElement
  */
private[jackson] final class JacksonBooleanElement(@volatile private var interValue: Boolean,
                                                   private val name: String) extends BooleanElement(interValue) {

  override def getName: String = name

}

object JacksonBooleanElement {

  def apply(jsonNode: JsonNode, name: String): JacksonBooleanElement = {
    jsonNode match {
      case jsonNode: BooleanNode =>
        val value = jsonNode.booleanValue()
        new JacksonBooleanElement(value, name)
      case _ =>
        throw new IllegalArgumentException(
          "the jsonNode param does not confirm to the correct type BooleanNode which JacksonBooleanElement needs")
    }
  }

}
