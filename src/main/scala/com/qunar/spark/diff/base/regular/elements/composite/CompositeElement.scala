package com.qunar.spark.diff.base.regular.elements.composite

import com.qunar.spark.diff.base.regular.elements.Element

/**
  * 递归结构中的复合元素(可继续拆分成[[CompositeElement]]或[[com.qunar.spark.diff.base.regular.elements.unit.UnitElement]])
  */
trait CompositeElement extends Element {

  /**
    * 列举出该复合元素下所有直接孩子Element
    */
  def childrenElements(): Seq[Element]

  /**
    * 重置该复合元素下的所有直接孩子Element
    */
  def setChildrenElements(newChildrenElements: Seq[Element]): Unit

}
