package com.qunar.spark.diff.base.compare.regular

import javax.validation.constraints.NotNull

import com.google.common.base.Preconditions
import com.qunar.spark.diff.base.regular.elements.Element

/**
  * 总的抽象的diff行为
  * (囊括[[com.qunar.spark.diff.base.regular.elements.UnitElement]]与[[com.qunar.spark.diff.base.regular.elements.CompositeElement]])
  *
  * @param decoratedDiffer 被装饰的前置比较器,不允许为null,否则构造失败,异常抛出
  */
abstract class AbstractDiffer(@NotNull private val decoratedDiffer: AbstractDiffer) extends Serializable {

  Preconditions.checkNotNull(decoratedDiffer)

  /**
    * [[com.qunar.spark.diff.base.compare.regular]]包内私有的默认构造器
    * 用于终结比较器构造链
    */
  private[regular] def this() = this(AbstractDiffer.defaultEmptyDiffer)

  /**
    * public api:比较两个Element是否相同
    * 方法首先执行[[decoratedDiffer.compare]]方法,若返回true则短路后续逻辑,返回false再执行自己的[[compareInternal]]
    * NOTICE: 为保证上述的逻辑被正确执行,此方法不可被重写
    *
    * @throws java.lang.IllegalArgumentException 详见[[com.qunar.spark.diff.base.compare.regular.unit.UnitDiffer]]
    * @return 传入的两个Element是否相同
    */
  @throws(classOf[IllegalArgumentException])
  final def compare(element1: Element, element2: Element): Boolean = {
    decoratedDiffer.compare(element1, element2) || compareInternal(element1, element2)
  }

  /**
    * 需要被子类实现的针对自身的比较逻辑
    */
  protected def compareInternal(element1: Element, element2: Element): Boolean

}

object AbstractDiffer {

  // public的默认空比较器静态object,供本类及子类使用
  val defaultEmptyDiffer = new DefaultEmptyDiffer

  /**
    * 默认空比较器,主要用于静态object,避免频繁实例化
    */
  class DefaultEmptyDiffer extends AbstractDiffer(new DefaultEmptyDiffer) {

    // 默认返回false即可
    override protected def compareInternal(element1: Element, element2: Element): Boolean = false

  }

}
