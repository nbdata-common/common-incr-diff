package com.qunar.spark.diff.base.compare.regular

import com.qunar.spark.diff.api._
import com.qunar.spark.diff.api.enums.DifferType
import com.qunar.spark.diff.api.enums.DifferType.DifferType
import com.qunar.spark.diff.base.compare.regular.annotation.support.{DiffIgnoreDiffer, DiffRangeDiffer, SortedArrayDiffer}
import com.qunar.spark.diff.base.compare.regular.composite.{CompositeDiffer, DefaultCompositeDiffer}
import com.qunar.spark.diff.base.compare.regular.unit.{DefaultUnitDiffer, EmptyUnitDiffer, UnitDiffer}
import com.qunar.spark.diff.base.regular.elements.unit.UnitElement
import com.qunar.spark.diff.base.regular.elements.Element
import com.qunar.spark.diff.base.regular.elements.composite.CompositeElement

/**
  * 递归结构中的diff比较器(总控与路由)
  * 此类被定义为密封抽象类,因为其继承类已经在本文件中被定义好,不需要在外部文件中拓展继承结构
  * 而本类的两个子类都以匿名类的形式构造,他们及时获取[[Differ]]的快照并及时重写[[differChainSnapshot]]方法
  *
  * @param unitDiffer      用于[[UnitElement]]的比较器
  * @param compositeDiffer 用于[[CompositeElement]]的比较器
  *                        <p/>
  * @note [[unitDiffer]]与[[compositeDiffer]]两个成员都没有被指明为具体的类型[[UnitDiffer]]与[[CompositeDiffer]],
  *       因为针对[[UnitElement]]或[[CompositeElement]]类型的比较器链中的每一个比较器的类型并非都是[[UnitDiffer]]
  *       或[[CompositeDiffer]],比如[[DiffIgnoreDiffer]]这样的注解增强Differ.在将来的拓展中,这种情况就更为显著.
  *       所以这里只将其限定为[[AbstractDiffer]].
  */
private[diff] sealed abstract class Differ private(private val unitDiffer: AbstractDiffer,
                                                   private val compositeDiffer: AbstractDiffer) {

  /**
    * 针对传入的两个[[Element]],作模式匹配分别路由到[[UnitDiffer]]或[[CompositeDiffer]]
    *
    * @throws IllegalArgumentException 详见[[UnitDiffer]]
    */
  @throws(classOf[IllegalArgumentException])
  def compare[T](element1: Element, element2: Element): Boolean = {
    (element1, element2) match {
      case elements: (UnitElement[_], UnitElement[_]) => unitDiffer.compare(elements._1, elements._2)
      case elements: (CompositeElement, CompositeElement) => compositeDiffer.compare(elements._1, elements._2)
      case _ => false
    }
  }

  /**
    * 返回一个比较器链的构成快照(即[[DifferType]]类型的一个序列)
    * 此方法供[[Differ]]类的引用者作内部结构优化
    */
  def differChainSnapshot: Seq[DifferType]

}

private[diff] object Differ {

  /**
    * 构造一个默认的[[Differ]]
    */
  def apply(): Differ = new Differ(DifferGenFactory.genDefaultUnitDiffer, DifferGenFactory.genDefaultCompositeDiffer) {

    /**
      * 默认的[[Differ]]快照即是默认的[[DifferType]]下面的枚举类型
      */
    override def differChainSnapshot: Seq[DifferType] = Seq(DifferType.UNIT_DEFAULT, DifferType.UNIT_DIFF_IGNORE,
      DifferType.UNIT_DIFF_RANGE, DifferType.COMPOSITE_DEFAULT, DifferType.COMPOSITE_DIFF_IGNORE)

  }

  /**
    * 构造一个定制化的[[Differ]]
    */
  def apply(unitDifferTypes: Seq[UnitDifferType], compositeDifferTypes: Seq[CompositeDifferType]): Differ = {
    new Differ(DifferGenFactory.genUnitDiffer(unitDifferTypes), DifferGenFactory.genCompositeDiffer(compositeDifferTypes)) {

      /**
        * 将开发者定制的[[unitDifferTypes]]与[[compositeDifferTypes]]的类型作合并成为快照
        */
      override def differChainSnapshot: Seq[DifferType] = {
        unitDifferTypes ++: compositeDifferTypes
      }

    }
  }

}

/**
  * 用于构造比较器链的工厂
  */
private[compare] object DifferGenFactory {

  /**
    * 生成一个针对[[UnitElement]]类型的比较器链
    */
  def genUnitDiffer(unitDifferTypes: Seq[UnitDifferType]): AbstractDiffer = {
    // 初始化一个可变类型的differ为空比较器
    // NOTICE:  空比较器作为链表头节点是强制要求,必须被加入比较链中
    var differ: AbstractDiffer = new EmptyUnitDiffer

    for (differType <- unitDifferTypes) {
      differType match {
        case DifferType.UNIT_DEFAULT => differ = new DefaultUnitDiffer(differ)
        case DifferType.UNIT_DIFF_IGNORE => differ = new DiffIgnoreDiffer(differ)
        case DifferType.UNIT_DIFF_RANGE => differ = new DiffRangeDiffer(differ)
      }
    }

    differ
  }

  /**
    * 生成一个针对[[CompositeElement]]类型的比较器链
    */
  def genCompositeDiffer(compositeDifferTypes: Seq[CompositeDifferType]): AbstractDiffer = {
    // 初始化一个可变类型的differ为AbstractDiffer内定义的默认空比较器
    // NOTICE:  空比较器作为链表头节点是强制要求,必须被加入比较链中
    var differ: AbstractDiffer = AbstractDiffer.defaultEmptyDiffer

    for (differType <- compositeDifferTypes) {
      differType match {
        case DifferType.COMPOSITE_DEFAULT => differ = new DefaultCompositeDiffer(differ)
        case DifferType.COMPOSITE_DIFF_IGNORE => differ = new DiffIgnoreDiffer(differ)
        case DifferType.COMPOSITE_SORT => differ = new SortedArrayDiffer(differ)
      }
    }

    differ
  }

  /**
    * 生成默认的针对[[UnitElement]]类型的比较器链
    */
  def genDefaultUnitDiffer: AbstractDiffer = {
    genUnitDiffer(Seq(DifferType.UNIT_DEFAULT, DifferType.UNIT_DIFF_IGNORE, DifferType.UNIT_DIFF_RANGE))
  }

  /**
    * 生成默认的针对[[CompositeElement]]类型的比较器链
    */
  def genDefaultCompositeDiffer: AbstractDiffer = {
    genCompositeDiffer(Seq(DifferType.COMPOSITE_DEFAULT, DifferType.COMPOSITE_DIFF_IGNORE))
  }

}
