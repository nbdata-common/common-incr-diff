package com.qunar.spark.diff.base.compare.annotation.support

import javax.validation.constraints.NotNull

import com.qunar.spark.diff.base.compare.Differ
import com.qunar.spark.diff.base.regular.elements.UnitElement

/**
  * 带有注解拦截的diff比较器
  */
abstract class AnnotationInterceptDiffer(@NotNull private val decoratedDiffer: Differ) extends Differ(decoratedDiffer) {

  /**
    * 判断当前注解是否对指定元素生效
    *
    * 此方法不可被重写,固定逻辑如下:
    * 1. 判断指定元素所对应的Field是否适用于该注解
    * 2. 判断指定元素所对应的Field是否拥有此注解
    */
  private final def isAnnotationEffective[T <: Comparable[T]](element: UnitElement[T]): Boolean = {
    isFieldHasAnnotation(element) && isAnnotationApplicableForField(element)
  }

  /**
    * 判断指定元素所对应的Field是否适用于该注解
    */
  protected def isAnnotationApplicableForField[T <: Comparable[T]](element: UnitElement[T]): Boolean

  /**
    * 判断指定元素所对应的Field是否拥有此注解
    */
  protected def isFieldHasAnnotation[T <: Comparable[T]](element: UnitElement[T]): Boolean

  /**
    * 判断在当前注解的特定规则下,两个待比较对象是否不同
    */
  protected def isDifferentUnderAnnotation[T <: Comparable[T]](element1: UnitElement[T], element2: UnitElement[T]): Boolean

  /**
    * 注解拦截diff比较器的AnnotationInterceptDiffer#isDifferent方法的标准实现
    * 此方法不允许被重写,子类的isDifferent都必须遵从此方法的逻辑:
    * 1.被装饰的differ的diff判断  2.注解是否是有效的  3.在注解规则下两个对象是否不同
    */
  override final def isDifferent[T <: Comparable[T]](element1: UnitElement[T], element2: UnitElement[T]): Boolean = {
    decoratedDiffer.isDifferent(element1, element2) || (isAnnotationEffective(element1) && isDifferentUnderAnnotation(element1, element2))
  }

}
