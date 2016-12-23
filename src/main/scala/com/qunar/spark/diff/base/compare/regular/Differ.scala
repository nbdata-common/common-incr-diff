package com.qunar.spark.diff.base.compare.regular

import javax.validation.constraints.NotNull

import com.google.common.base.Preconditions
import com.qunar.spark.diff.base.regular.elements.UnitElement

/**
  * diff比较器的行为抽象
  *
  * @param decoratedDiffer 被装饰的前置比较器,不允许为null,否则构造失败,异常抛出
  */
abstract class Differ(@NotNull private val decoratedDiffer: Differ) {

  Preconditions.checkNotNull(decoratedDiffer)

  /**
    * 用于比较两个UnitElement是否不同
    * 适用于此方法的类型包括:NumericElement,TextElement,BooleanElement
    *
    * @throws java.lang.IllegalArgumentException 当传入的两个UnitElement的实际类型不同时抛出此异常
    */
  @throws(classOf[IllegalArgumentException])
  def isDifferent[T <: Comparable[T]](element1: UnitElement[T], element2: UnitElement[T]): Boolean

}

object Differ {

  def apply(): Differ = DifferFactory.generateDiffer

}

private[compare] object DifferFactory {

  def generateDiffer: Differ = {
    null
  }

}
