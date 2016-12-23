package com.qunar.spark.diff.base.compare.regular.annotation.support

import javax.validation.constraints.NotNull

import com.qunar.spark.diff.base.compare.regular.Differ
import com.qunar.spark.diff.base.regular.elements.UnitElement
import com.qunar.spark.diff.ext.AnnotationAware

/**
  * 带有注解增强功能的diff比较器的通用抽象
  *
  */
abstract class AnnotationAdvancedDiffer(@NotNull private val decoratedDiffer: Differ) extends Differ(decoratedDiffer) {

  /**
    * 判断当前注解是否对指定元素生效
    *
    * 此方法不可被重写,注解是否对元素生效主要包括两个方面:
    * 1. 指定元素所对应的Field是否适用于该注解
    * 2. 指定元素所对应的Field是否拥有此注解
    */
  private final def isAnnotationEffectiveForElement(element: AnnotationAware): Boolean = {
    isAnnotationApplicableForElement(element) && isElementHasAnnotation(element)
  }

  /**
    * 判断指定元素所对应的Field是否适用于该注解
    */
  protected def isAnnotationApplicableForElement(element: AnnotationAware): Boolean

  /**
    * 判断指定元素所对应的Field是否拥有此注解
    */
  protected def isElementHasAnnotation(element: AnnotationAware): Boolean

  /**
    * 判断在当前注解的特定规则下,两个待比较对象是否不同
    */
  protected def isDifferentUnderAnnotation[T <: Comparable[T]](element1: UnitElement[T], element2: UnitElement[T]): Boolean

  /**
    * 判断元素是否带有注解感知功能
    */
  private def isElementAnnotationAware[T <: Comparable[T]](element: UnitElement[T]): Boolean = {
    element.isInstanceOf[AnnotationAware]
  }

  /**
    * 注解增强的diff比较器:对[[com.qunar.spark.diff.base.compare.regular.Differ.isDifferent]]方法的标准实现
    *
    * NOTICE: 为了保持和Differ的继承关系,此方法的入参仍然需要保持原有类型UnitElement[T <\: Comparable[T]].
    * 所以,为了确保传入的参数能够感知注解(实现AnnotationAware),本方法在内部实现了AnnotationAware类型的判断逻辑.
    * 如果参数不是AnnotationAware类型,后面基于注解增强的逻辑将被false短路.
    * 之所以这样设计,主要是考虑到我们的Differ模块被设计为"链式"的(依托于装饰器模式),当自己返回false时并不会对全局
    * result产生"一票否决"式的影响,仅当自己返回true时才能对全局result产生"一锤定音"式的效果.换句话说,在这样的
    * "比较链"中插拔一个返回false的Differ,对整体的isDifferent方法是"free of side-effect"的.
    * 所以,当传入一个非AnnotationAware时返回false以消除side-effect,使我们可以放心地在比较器链中添加
    * AnnotationAdvancedDiffer.
    *
    * 此方法不允许被重写,子类的isDifferent都必须遵从此方法的逻辑:
    * 1. 被装饰的前置differ的diff判断 ||
    * 2. 判断方法传入的元素是否有注解感知能力 &&
    * 3. 判断当前注解是否对方法传入的元素生效 &&
    * 4. 判断在注解规则下两个对象是否不同
    *
    * 其中,第一点与后面三点是或的关系,后面三点之间是与的关系
    */
  override final def isDifferent[T <: Comparable[T]](element1: UnitElement[T], element2: UnitElement[T]): Boolean = {
    decoratedDiffer.isDifferent(element1, element2) ||
      isElementAnnotationAware(element1) &&
        isAnnotationEffectiveForElement(element1.asInstanceOf[AnnotationAware]) &&
        isDifferentUnderAnnotation(element1, element2)
  }

}
