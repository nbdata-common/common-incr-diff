package com.qunar.spark.diff.base.regular.elements

/**
  * 在规则(递归)结构中,表征一个元素(单元节点或复合节点)
  * UnitElement和CompositeElement的父类
  */
trait Element extends Comparable[Element] with Serializable {

  /**
    * (在指定的diff目标中)该元素的唯一标识
    */
  def getName: String

  /**
    * 最原始的Element之间的比较:就是比较两个Element的[[getName]]是否相同
    */
  override def compareTo(elem: Element): Int = {
    if (this.getName.equals(elem.getName)) 0 else 1
  }

}
