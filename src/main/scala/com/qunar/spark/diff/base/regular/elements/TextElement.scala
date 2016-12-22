package com.qunar.spark.diff.base.regular.elements

/**
  * 文本类型的原子元素:其唯一映射到一个String类型的值
  */
abstract class TextElement(private val interValue: String) extends UnitElement[String] {

  override def value: String = interValue

}
