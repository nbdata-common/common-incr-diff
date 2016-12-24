package com.qunar.spark.diff.base.compare.regular.annotation.support

import javax.validation.constraints.NotNull

import com.qunar.spark.diff.base.compare.regular.Differ
import com.qunar.spark.diff.base.regular.elements.UnitElement
import com.qunar.spark.diff.ext.AnnotationAware

/**
  * 针对[[com.qunar.spark.diff.api.annotation.DiffRange]]注解的拓展比较器
  */
class DiffRangeDiffer(@NotNull private val decoratedDiffer: Differ) extends AnnotationAdvancedDiffer(decoratedDiffer) {

  override protected def isAnnotationApplicableForElement(element: AnnotationAware): Boolean = {
    false
  }

  override protected def isElementHasAnnotation(element: AnnotationAware): Boolean = {
    false
  }

  override protected def isDifferentUnderAnnotation[T <: Comparable[T]](element1: UnitElement[T], element2: UnitElement[T]): Boolean = {
    false
  }

}
