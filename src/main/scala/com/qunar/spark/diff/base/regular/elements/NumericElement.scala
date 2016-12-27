package com.qunar.spark.diff.base.regular.elements

import com.qunar.spark.diff.base.GenericNumber

/**
  * 数值类型的原子元素:其唯一映射到一个数值类型的值
  */
abstract class NumericElement(@volatile private var interValue: GenericNumber) extends UnitElement[GenericNumber] {

  override def value: GenericNumber = interValue

  override def setValue(newValue: GenericNumber): Unit = {
    this.interValue = newValue
  }

}
