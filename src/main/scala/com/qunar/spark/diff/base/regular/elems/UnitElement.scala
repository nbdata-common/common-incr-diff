package com.qunar.spark.diff.base.regular.elems

/**
  * 规则(递归)结构中的原子元素(不可继续拆分)
  */
trait UnitElement extends Serializable {

  /**
    * (在指定的diff目标中)该元素的唯一标识
    */
  def name: String

  def value[@specialized(Char, Int, Boolean, Byte, Long, Float, Double) T]: T

}
