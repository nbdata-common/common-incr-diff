package com.qunar.spark.diff.internal.impl.regular.jackson.element

import com.qunar.spark.diff.base.regular.elements.Element
import com.qunar.spark.diff.base.regular.elements.composite.ArrayElement

/**
  * 适用于Jackson的[[ArrayElement]]
  */
private[jackson] final class JacksonArrayElement(@volatile private var interChildrenElements: Seq[Element],
                                                 private val name: String) extends ArrayElement {

  def this(name: String) = this(Seq(), name)

  override def childrenElements(): Seq[Element] = interChildrenElements

  override def setChildrenElements(newElements: Seq[Element]): Unit = {
    this.interChildrenElements = newElements
  }

  override def getName: String = name

}

private[jackson] object JacksonArrayElement {

  def apply(name: String): JacksonArrayElement = {
    new JacksonArrayElement(name)
  }

  def apply(childrenElements: Seq[Element], name: String): JacksonArrayElement = {
    new JacksonArrayElement(childrenElements, name)
  }

}