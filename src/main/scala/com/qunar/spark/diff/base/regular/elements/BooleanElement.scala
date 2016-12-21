package com.qunar.spark.diff.base.regular.elements

import java.lang.Boolean

/**
  * 布尔类型的原子元素:其唯一映射到一个Boolean类型的值
  */
abstract class BooleanElement(private val interValue: Boolean) extends UnitElement[Boolean] {

  override def value: Boolean = interValue

}
