package com.qunar.spark.diff.base.regular.elements

/**
  * 规则(递归)结构中的复合元素(可继续拆分成Composite或Unit)
  */
trait CompositeElement extends Element {

  /**
    * 对该node下所有直接子node按给定方法排序
    */
  def sortElement(): Seq[Element]

}
