package com.qunar.spark.diff.base.compare.regular.unit

import com.qunar.spark.diff.base.regular.elements.unit.UnitElementType.UnitElementType
import com.qunar.spark.diff.base.regular.elements.unit.UnitElement

/**
  * 针对[[UnitElement]]的空比较器
  * 这里需要重新定义一个类似于[[com.qunar.spark.diff.base.compare.regular.AbstractDiffer.defaultEmptyDiffer]]
  * 的空比较器的原因在于我们需要在链表的头节点处校验传入的参数的类型是否一致,而它的decoratedDiffer正是
  * [[com.qunar.spark.diff.base.compare.regular.AbstractDiffer.defaultEmptyDiffer]]
  *
  * NOTICE: 此类不允许被继承
  */
final class EmptyUnitDiffer extends UnitDiffer {

  override def compareUnitElement(element1: UnitElement[_], element2: UnitElement[_]): Boolean = {
    checkTypeConsistent(element1, element2)
  }

  /**
    * 检查传入的两个参数是否拥有一致的实际类型
    * 如果不一致,则抛出[[IllegalArgumentException]]
    * 否则返回false,以确保比较器链正常向下游传递
    */
  private def checkTypeConsistent(element1: UnitElement[_], element2: UnitElement[_]): Boolean = {
    val elemType1: UnitElementType = UnitElement.actualType(element1)
    val elemType2: UnitElementType = UnitElement.actualType(element2)
    if (elemType1 != elemType2) {
      throw new IllegalArgumentException("element1 and element2 don't share the same actual type")
    }

    false
  }

}
