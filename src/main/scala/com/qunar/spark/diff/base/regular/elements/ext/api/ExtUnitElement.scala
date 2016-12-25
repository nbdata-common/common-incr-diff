package com.qunar.spark.diff.base.regular.elements.ext.api

import com.qunar.spark.diff.base.regular.elements.UnitElement

/**
  * UnitElement的拓展接口
  *
  * 引入一个被装饰者decoratedElement,并在其上包装方法,拓展功能
  */
abstract class ExtUnitElement[T <: Comparable[T]](private val decoratedElement: UnitElement[T]) extends UnitElement[T] {

  override def value: T = decoratedElement.value

  override def getName: String = decoratedElement.getName

}
