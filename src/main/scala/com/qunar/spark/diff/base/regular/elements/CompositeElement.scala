package com.qunar.spark.diff.base.regular.elements

/**
  * 规则(递归)结构中的复合元素(可继续拆分成Composite或Unit)
  */
trait CompositeElement extends Element {

  /**
    * 列举出该复合元素下所有直接孩子Element
    */
  def listChildrenElements(): Seq[Element]

}
