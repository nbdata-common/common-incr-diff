package com.qunar.spark.diff.internal.impl.regular.jackson.element

import com.qunar.spark.diff.base.regular.elements.{CompositeElement, Element}

/**
  * 适用于Jackson的CompositeElement
  */
private[jackson] final class JacksonCompositeElement(private val childrenElements: Seq[Element],
                                                     private val name: String) extends CompositeElement {

  override def listChildrenElements(): Seq[Element] = childrenElements

  override def getName: String = name

}

private[jackson] object JacksonCompositeElement {

  def apply(): JacksonCompositeElement = {

  }

}
