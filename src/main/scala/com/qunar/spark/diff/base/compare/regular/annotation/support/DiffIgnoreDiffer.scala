package com.qunar.spark.diff.base.compare.regular.annotation.support

import javax.validation.constraints.NotNull

import com.qunar.spark.diff.api.annotation.DiffIgnore
import com.qunar.spark.diff.base.compare.regular.{AbstractDiffer, AnnotationAdvancedDiffer}
import com.qunar.spark.diff.base.regular.elements.Element
import com.qunar.spark.diff.ext.AnnotationAware

/**
  * 针对[[com.qunar.spark.diff.api.annotation.DiffIgnore]]注解的拓展比较器
  */
class DiffIgnoreDiffer(@NotNull private val decoratedDiffer: AbstractDiffer) extends AnnotationAdvancedDiffer(decoratedDiffer) {

  /**
    * 对于[[com.qunar.spark.diff.api.annotation.DiffIgnore]]注解,
    * 任何字段,无论是基本类型,还是复合类型,只要被打上该注解,就一律忽略.
    * 所以,这里无条件返回true
    */
  override protected def isAnnotationApplicableForElement(element: AnnotationAware): Boolean = true

  override protected def isElementHasAnnotation(element: AnnotationAware): Boolean = {
    val annotations = element.allAnnotations
    for (annotation <- annotations) {
      if (annotation.isInstanceOf[DiffIgnore]) true
    }
    false
  }

  /**
    * 对于[[com.qunar.spark.diff.api.annotation.DiffIgnore]]注解,
    * 字段被忽略的意义就是两个字段的比较结果是相同,即这里应该无条件返回true
    */
  override protected def isSameUnderAnnotation(element1: Element, element2: Element): Boolean = true

}
