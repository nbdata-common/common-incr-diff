package com.qunar.spark.diff.internal.impl.regular.jackson.element

import com.qunar.spark.diff.base.regular.elements.TextElement

/**
  * 适用于Jackson的TextElement
  */
private[jackson] final class JacksonTextElement(private val interValue: String,
                                                private val name: String) extends TextElement(interValue) {

  override def getName: String = name

}
