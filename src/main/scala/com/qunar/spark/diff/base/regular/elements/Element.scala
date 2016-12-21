package com.qunar.spark.diff.base.regular.elements

/**
  * 在规则(递归)结构中,表征一个元素(单元节点或复合节点)
  * UnitElement和CompositeElement的父类
  */
trait Element extends Serializable {

  /**
    * (在指定的diff目标中)该元素的唯一标识
    */
  def name: String

}
