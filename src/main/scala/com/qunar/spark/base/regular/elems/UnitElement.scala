package com.qunar.spark.base.regular.elems

/**
  * 规则结构(递归结构)的原子元素(不可继续拆分)
  */
trait UnitElement extends Serializable {

  /**
    * (在指定的目标中)该元素的唯一标识
    */
  def name: String

  def value[@specialized(Char, Int, Boolean, Byte, Long, Float, Double) T]: T

}
