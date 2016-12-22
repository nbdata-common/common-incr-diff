package com.qunar.spark.diff.base.compare

import javax.validation.constraints.NotNull

import com.qunar.spark.diff.base.regular.elements.UnitElement

/**
  * 默认diff比较器
  */
class DefaultDiffer(@NotNull private val decorateDiffer: Differ) extends Differ(decorateDiffer) {

  /**
    * 默认的比较行为即是比较两个element.value是否不相等
    *
    * @return 不相等:true  相等:false
    */
  override def isDifferent[T <: Comparable[T]](element1: UnitElement[T], element2: UnitElement[T]): Boolean = {
    decorateDiffer.isDifferent(element1, element2) || element1.value.compareTo(element2.value) != 0
  }

}
