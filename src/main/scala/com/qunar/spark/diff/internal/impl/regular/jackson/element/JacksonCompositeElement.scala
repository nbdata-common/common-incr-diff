package com.qunar.spark.diff.internal.impl.regular.jackson.element

import com.qunar.spark.diff.base.regular.elements.Element
import com.qunar.spark.diff.base.regular.elements.composite.CompositeElement

/**
  * 适用于Jackson的[[CompositeElement]]
  */
private[jackson] final class JacksonCompositeElement(@volatile private var interChildrenElements: Seq[Element],
                                                     private val name: String) extends CompositeElement {

  def this(name: String) = this(Seq(), name)

  override def childrenElements(): Seq[Element] = interChildrenElements

  override def setChildrenElements(newElements: Seq[Element]): Unit = {
    this.interChildrenElements = newElements
  }

  override def getName: String = name

}

private[jackson] object JacksonCompositeElement {

  def apply(name: String): JacksonCompositeElement = {
    new JacksonCompositeElement(name)
  }

  def apply(childrenElements: Seq[Element], name: String): JacksonCompositeElement = {
    new JacksonCompositeElement(childrenElements, name)
  }

}
