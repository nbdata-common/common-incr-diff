package com.qunar.spark.diff.base.compare.regular

import javax.validation.constraints.NotNull

import com.qunar.spark.diff.base.regular.elements.Element
import com.qunar.spark.diff.ext.AnnotationAware

/**
  * 带有注解增强功能的diff比较器的通用抽象
  * 在比较器链中添加此类型的Differ可以拦截带特定的注解的element并作增强服务
  *
  * 实现一个带注解增强的differ需要继承该抽象类,并实现:
  * [[isAnnotationApplicableForElement]],[[isElementHasAnnotation]],[[isSameUnderAnnotation]]
  * 三个方法即可.
  */
abstract class AnnotationAdvancedDiffer(@NotNull private val decoratedDiffer: AbstractDiffer) extends AbstractDiffer(decoratedDiffer) {

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
    * 判断在当前注解的特定规则下,两个待比较对象是否相同
    */
  protected def isSameUnderAnnotation(element1: Element, element2: Element): Boolean

  /**
    * 判断元素是否带有注解感知功能
    */
  private def isElementAnnotationAware(element: Element): Boolean = {
    element.isInstanceOf[AnnotationAware]
  }

  /**
    * 注解增强的diff比较器:对[[com.qunar.spark.diff.base.compare.regular.AbstractDiffer.compareInternal]]方法的标准实现
    *
    * NOTICE: 为了保持和[[AbstractDiffer]]的继承关系,此方法的入参仍然需要保持原有类型[[Element]].
    * 所以,为了确保传入的参数能够感知注解(实现[[AnnotationAware]]),本方法在内部实现了[[AnnotationAware]]类型的判断逻辑:
    * [[isElementAnnotationAware]]--如果参数不是[[AnnotationAware]]类型,后面基于注解增强的逻辑将被'''false'''短路.
    * 之所以这样设计,主要是考虑到我们的[[AbstractDiffer]]模块被设计为''"链式"''的(依托于装饰器模式),当自己返回false时并不会对全局
    * result产生''"一票否决"''式的影响,仅当自己返回true时才能对全局result产生''"一锤定音"''式的效果.换句话说,在这样的
    * "比较链"中插拔一个返回false的[[AbstractDiffer]],对整体的[[AbstractDiffer.compare]]方法是'''"free of side-effect"'''的.
    * 所以,当传入一个非[[AnnotationAware]]时这里返回false以消除'''"side-effect"''',使我们可以放心地在比较器链中添加
    * [[AnnotationAdvancedDiffer]].
    *
    * 此方法不允许被重写,子类的[[compareInternal]]都必须遵从此方法的逻辑:
    * 1. 判断方法传入的元素是否有注解感知能力 &&
    * 2. 判断当前注解是否对方法传入的元素生效 &&
    * 3. 判断在注解规则下两个对象是否不同
    */
  override protected final def compareInternal(element1: Element, element2: Element): Boolean = {
    isElementAnnotationAware(element1) &&
      isAnnotationEffectiveForElement(element1.asInstanceOf[AnnotationAware]) &&
      isSameUnderAnnotation(element1, element2)
  }

}
