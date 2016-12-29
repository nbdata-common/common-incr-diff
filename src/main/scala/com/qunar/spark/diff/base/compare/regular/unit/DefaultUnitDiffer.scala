package com.qunar.spark.diff.base.compare.regular.unit

import javax.validation.constraints.NotNull

import com.qunar.spark.diff.base.regular.elements.UnitElement

/**
  * UnitElement的默认diff比较器
  */
class DefaultUnitDiffer(@NotNull private val decoratedDiffer: UnitDiffer) extends UnitDiffer(decoratedDiffer) {

  /**
    * UnitElement默认的比较行为即是比较两个element是否相等
    */
  override def compareUnitElement(element1: UnitElement[_], element2: UnitElement[_]): Boolean = {
    element1.compareTo(element2) == 0
  }

}
