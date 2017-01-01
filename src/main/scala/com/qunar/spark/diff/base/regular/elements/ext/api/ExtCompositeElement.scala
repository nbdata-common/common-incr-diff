package com.qunar.spark.diff.base.regular.elements.ext.api

import com.qunar.spark.diff.base.regular.elements.Element
import com.qunar.spark.diff.base.regular.elements.composite.CompositeElement

/**
  * CompositeElement的拓展接口
  *
  * 引入一个被装饰者decoratedElement,并在其上包装方法,拓展功能
  */
abstract class ExtCompositeElement(private val decoratedElement: CompositeElement) extends CompositeElement {

  override def childrenElements(): Seq[Element] = decoratedElement.childrenElements()

  override def getName: String = decoratedElement.getName

}
