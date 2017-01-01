package com.qunar.spark.diff.base.compare.regular.composite

import javax.validation.constraints.NotNull

import com.qunar.spark.diff.base.compare.regular.AbstractDiffer
import com.qunar.spark.diff.base.regular.elements.composite.CompositeElement

/**
  * [[CompositeElement]]的默认diff比较器
  */
class DefaultCompositeDiffer(@NotNull private val decoratedDiffer: AbstractDiffer) extends CompositeDiffer(decoratedDiffer) {

  /**
    * [[CompositeElement]]默认的比较行为即是比较两个element是否相等
    */
  override protected def compareCompositeElement(element1: CompositeElement, element2: CompositeElement): Boolean = {
    element1.compareTo(element2) == 0
  }

}
