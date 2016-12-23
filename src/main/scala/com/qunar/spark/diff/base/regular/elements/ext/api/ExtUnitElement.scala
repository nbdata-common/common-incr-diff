package com.qunar.spark.diff.base.regular.elements.ext.api

import com.qunar.spark.diff.base.regular.elements.UnitElement

/**
  * 拓展的UnitElement
  *
  * 使用装饰器模式,以解决
  */
abstract class ExtUnitElement[T <: Comparable[T]](private val decoratedElement: UnitElement[T]) extends UnitElement[T] {


}
