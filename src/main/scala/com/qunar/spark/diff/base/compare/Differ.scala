package com.qunar.spark.diff.base.compare

import javax.validation.constraints.NotNull

import com.qunar.spark.diff.base.regular.elements.UnitElement

/**
  * diff比较器的行为抽象
  */
abstract class Differ(@NotNull private val decorateDiffer: Differ) {

  @throws(classOf[IllegalArgumentException])
  def isDifferent[T <: Comparable[T]](element1: UnitElement[T], element2: UnitElement[T]): Boolean

}

object Differ {

  def apply(): Differ = DifferFactory.generateDiffer

}

object DifferFactory {

  def generateDiffer: Differ = {

  }

}
