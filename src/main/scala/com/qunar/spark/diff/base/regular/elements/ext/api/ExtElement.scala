package com.qunar.spark.diff.base.regular.elements.ext.api

import com.qunar.spark.diff.base.regular.elements.Element

/**
  * Element的拓展接口
  *
  * 引入一个被装饰者decoratedElement,并在其上包装方法,拓展功能
  */
abstract class ExtElement(private val decoratedElement: Element) extends Element {

  override def getName: String = decoratedElement.getName

}
