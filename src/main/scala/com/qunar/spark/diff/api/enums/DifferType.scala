package com.qunar.spark.diff.api.enums

import scala.language.implicitConversions

/**
  * [[com.qunar.spark.diff.base.compare.regular.Differ]]的枚举类型
  * 用于定制化构造自己的Differ比较器链
  */
object DifferType extends Enumeration {

  type DifferType = Value

  /**
    * 适用于[[com.qunar.spark.diff.base.regular.elements.UnitElement]]的[[com.qunar.spark.diff.base.compare.regular.Differ]]类型
    */
  sealed class UnitDifferType extends Val

  val UNIT_DIFF_IGNORE = new UnitDifferType
  val UNIT_DIFF_RANGE = new UnitDifferType
  val UNIT_DEFAULT = new UnitDifferType

  implicit def convertToUnit(v: Value): UnitDifferType = v.asInstanceOf[UnitDifferType]

  /**
    * 适用于[[com.qunar.spark.diff.base.regular.elements.CompositeElement]]的[[com.qunar.spark.diff.base.compare.regular.Differ]]类型
    */
  sealed class CompositeDifferType extends Val

  val COMPOSITE_DIFF_IGNORE = new CompositeDifferType
  val COMPOSITE_SORT = new CompositeDifferType
  val COMPOSITE_DEFAULT = new CompositeDifferType

  implicit def convertToComposite(v: Value): CompositeDifferType = v.asInstanceOf[CompositeDifferType]

}
