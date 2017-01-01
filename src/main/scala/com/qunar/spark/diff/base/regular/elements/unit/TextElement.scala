package com.qunar.spark.diff.base.regular.elements.unit

/**
  * 文本类型的原子元素:其唯一映射到一个String类型的值
  */
abstract class TextElement(@volatile private var interValue: String) extends UnitElement[String] {

  override def value: String = interValue

  override def setValue(newValue: String): Unit = {
    this.interValue = newValue
  }

}
