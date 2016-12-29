package com.qunar.spark.diff.base.compare.regular

import com.qunar.spark.diff.base.compare.regular.composite.CompositeDiffer
import com.qunar.spark.diff.base.compare.regular.unit.UnitDiffer
import com.qunar.spark.diff.base.regular.elements.{CompositeElement, Element, UnitElement}

/**
  * 递归结构中的diff比较器(总控)
  *
  * @param unitDiffer      用于UnitElement的比较器
  * @param compositeDiffer 用于CompositeElement的比较器
  */
private[diff] final class Differ(private val unitDiffer: UnitDiffer,
                                 private val compositeDiffer: CompositeDiffer) {

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

  def apply(): Differ = new Differ(DifferFactory.genUnitDiffer, DifferFactory.genCompositeDiffer)

}

private[compare] object DifferFactory {

  def genUnitDiffer: UnitDiffer = {
    null
  }

  def genCompositeDiffer: CompositeDiffer = {
    null
  }

}
