package com.qunar.spark.diff.base.compare.regular

import com.qunar.spark.diff.api._
import com.qunar.spark.diff.api.enums.DifferType
import com.qunar.spark.diff.base.compare.regular.composite.CompositeDiffer
import com.qunar.spark.diff.base.compare.regular.unit.UnitDiffer
import com.qunar.spark.diff.base.regular.elements.{CompositeElement, Element, UnitElement}

/**
  * 递归结构中的diff比较器(总控与路由)
  *
  * @param unitDiffer      用于[[UnitElement]]的比较器
  * @param compositeDiffer 用于[[CompositeElement]]的比较器
  */
private[diff] final class Differ(private val unitDiffer: UnitDiffer,
                                 private val compositeDiffer: CompositeDiffer) {

  /**
    * 针对传入的两个[[Element]],作模式匹配分别路由到[[UnitDiffer]]或[[CompositeDiffer]]
    *
    * @throws IllegalArgumentException 详见[[com.qunar.spark.diff.base.compare.regular.unit.UnitDiffer]]
    */
  @throws(classOf[IllegalArgumentException])
  def compare[T](element1: Element, element2: Element): Boolean = {
    (element1, element2) match {
      case elements: (UnitElement[_], UnitElement[_]) => unitDiffer.compare(elements._1, elements._2)
      case elements: (CompositeElement, CompositeElement) => compositeDiffer.compare(elements._1, elements._2)
      case _ => false
    }
  }

}

private[diff] object Differ {

  /**
    * 构造一个默认的Differ
    */
  def apply(): Differ = new Differ(DifferFactory.genDefaultUnitDiffer, DifferFactory.genDefaultCompositeDiffer)

  /**
    * 构造一个定制化的Differ
    */
  def apply(unitDifferTypes: Seq[UnitDifferType], compositeDifferTypes: Seq[CompositeDifferType]): Differ = {
    new Differ(DifferFactory.genUnitDiffer(unitDifferTypes), DifferFactory.genCompositeDiffer(compositeDifferTypes))
  }

}

/**
  * 用于构造比较器链的工厂
  */
private[compare] object DifferFactory {

  def genUnitDiffer(unitDifferTypes: Seq[UnitDifferType]): UnitDiffer = {
    null
  }

  def genCompositeDiffer(compositeDifferTypes: Seq[CompositeDifferType]): CompositeDiffer = {
    null
  }

  def genDefaultUnitDiffer: UnitDiffer = {
    genUnitDiffer(null)
  }

  def genDefaultCompositeDiffer: CompositeDiffer = {
    genCompositeDiffer(null)
  }

}
