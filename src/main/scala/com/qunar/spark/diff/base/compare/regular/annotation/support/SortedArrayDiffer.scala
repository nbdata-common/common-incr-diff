package com.qunar.spark.diff.base.compare.regular.annotation.support

import javax.validation.constraints.NotNull

import com.qunar.spark.diff.api.annotation.Sorted
import com.qunar.spark.diff.base.compare.regular.{AbstractDiffer, AnnotationAdvancedDiffer}
import com.qunar.spark.diff.base.regular.elements.Element
import com.qunar.spark.diff.ext.AnnotationAware

/**
  * 针对[[com.qunar.spark.diff.api.annotation.Sorted]]注解的拓展比较器
  */
class SortedArrayDiffer(@NotNull private val decoratedDiffer: AbstractDiffer) extends AnnotationAdvancedDiffer(decoratedDiffer) {

  override protected def isAnnotationApplicableForElement(element: AnnotationAware): Boolean = {
    false
  }

  override protected def isElementHasAnnotation(element: AnnotationAware): Boolean = {
    val annotations = element.allAnnotations
    for (annotation <- annotations) {
      if (annotation.isInstanceOf[Sorted]) true
    }
    false
  }

  override protected def isSameUnderAnnotation(element1: Element, element2: Element): Boolean = {
    false
  }

}
