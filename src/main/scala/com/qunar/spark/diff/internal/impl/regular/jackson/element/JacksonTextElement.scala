package com.qunar.spark.diff.internal.impl.regular.jackson.element

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.TextNode
import com.qunar.spark.diff.base.regular.elements.TextElement

/**
  * 适用于Jackson的TextElement
  */
private[jackson] final class JacksonTextElement(@volatile private var interValue: String,
                                                private val name: String) extends TextElement(interValue) {

  override def getName: String = name

}

object JacksonTextElement {

  def apply(jsonNode: JsonNode, name: String): JacksonTextElement = {
    jsonNode match {
      case jsonNode: TextNode =>
        val value = jsonNode.textValue()
        new JacksonTextElement(value, name)
      case _ =>
        throw new IllegalArgumentException(
          "the jsonNode param does not confirm to the correct type TextNode which JacksonTextElement needs")
    }
  }

}
