package com.qunar.spark.diff.internal.impl.regular.jackson.element

import com.qunar.spark.diff.base.regular.elements.BooleanElement

/**
  * 适用于Jackson的BooleanElement
  */
private[jackson] final class JacksonBooleanElement(private val interValue: Boolean,
                                                   private val name: String) extends BooleanElement(interValue) {

  override def getName: String = name

}
