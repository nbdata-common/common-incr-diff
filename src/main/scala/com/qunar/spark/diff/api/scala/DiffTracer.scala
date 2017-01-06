package com.qunar.spark.diff.api.scala

import com.fasterxml.jackson.databind.JsonNode
import com.qunar.spark.diff.api.enums.{ComparableType, DifferType}
import com.qunar.spark.diff.api.enums.ComparableType.ComparableType
import com.qunar.spark.diff.api.{CompositeDifferType, UnitDifferType}
import com.qunar.spark.diff.base.compare.regular.Differ
import com.qunar.spark.diff.base.sort.Sorter
import com.qunar.spark.diff.internal.impl.regular.jackson.JacksonDiffTracer

import scala.collection.mutable.ArrayBuffer
import scala.reflect.ClassTag

/**
  * incr-diff的总的泛化抽象
  */
trait DiffTracer[T] extends Serializable {

  /**
    * 比较两个实体 targetLeft 与 targetRight 是否是不同的
    * 这里两个实体将被当作Plain Ordinary Java Object
    *
    * NOTICE: 此方法开启了注解增强功能,可在[[T]]所对应Class的字段上使用以下包内的注解:
    *
    * @see [[com.qunar.spark.diff.api.annotation]]
    *      <p/>
    * @param targetLeft  第一个待比较对象
    * @param targetRight 第二个待比较对象
    * @return targetLeft 与 targetRight 是否是相同的
    */
  def isDifferent(targetLeft: T, targetRight: T): Boolean

}

/**
  * scala api: DifferTracer工厂
  */
object DiffTracer {

  /**
    * 默认的[[DiffTracer]]实现:[[JacksonDiffTracer]]
    */
  def apply[T: ClassTag](): DiffTracer[T] = new JacksonDiffTracer[T]

  /**
    * 返回一个[[Builder]]用于定制化构造[[DiffTracer]]
    * NOTICE: 默认返回一个构建[[JacksonDiffTracer]]的[[Builder]]
    */
  def builder[T: ClassTag](buildType: ComparableType = ComparableType.JACKSON): Builder[T] = new Builder(buildType)

  /**
    * 用于构造[[DiffTracer]]的建造者,可以自己customize比较器链
    * [[com.qunar.spark.diff.base.compare.regular.unit.UnitDiffer]]
    * 与[[com.qunar.spark.diff.base.compare.regular.composite.CompositeDiffer]]
    * 的构成成员
    * <p/>
    * for example: <pre> {
    *
    * val diffTracer = DiffTracer.builder[T]()
    * * .setUnitDifferTypes(DifferType.UNIT_DIFF_IGNORE, DifferType.UNIT_DEFAULT)
    * * .setCompositeDifferTypes(DifferType.COMPOSITE_DEFAULT)
    * * .build
    *
    * } </pre>
    * <p/>
    * NOTICE: 对于一般的情况,默认的[[apply]]方法已经足够使用
    */
  class Builder[T: ClassTag](private val buildType: ComparableType) {

    // 分别用于建造UnitDiffer与CompositeDiffer
    private val unitDifferTypes = ArrayBuffer.newBuilder[UnitDifferType]
    private val compositeDifferTypes = ArrayBuffer.newBuilder[CompositeDifferType]

    //容量预加载
    unitDifferTypes.sizeHint(3)
    compositeDifferTypes.sizeHint(3)

    /**
      * 以可变参数列表的形式指定加入[[com.qunar.spark.diff.base.compare.regular.unit.UnitDiffer]]比较器链
      * 的比较器种类.
      * <p/>
      * NOTICE: [[UnitDifferType*]]类型的可变参数将被转换为一个[[Seq]],送往比较器链构造工厂后将按照序列中给出
      * 的类型顺序构造比较器链,[[setCompositeDifferTypes]]方法同理.
      */
    def setUnitDifferTypes(differTypes: UnitDifferType*): this.type = {
      for (differType <- differTypes) {
        unitDifferTypes += differType
      }
      this
    }

    /**
      * 以可变参数列表的形式指定加入[[com.qunar.spark.diff.base.compare.regular.composite.CompositeDiffer]]
      * 比较器链的比较器种类.
      */
    def setCompositeDifferTypes(differTypes: CompositeDifferType*): this.type = {
      for (differType <- differTypes) {
        compositeDifferTypes += differType
      }
      this
    }

    // 返回Unit比较器链类型的序列
    private def unitResult: Seq[UnitDifferType] = unitDifferTypes.result

    // 返回Composite比较器链类型的序列
    private def compositeResult: Seq[CompositeDifferType] = compositeDifferTypes.result

    /**
      * 依据指定的[[buildType]]构建[[DiffTracer]]
      */
    def build: DiffTracer[T] = {
      val differ = Differ(unitResult, compositeResult)
      val sorter = Sorter()
      buildType match {
        case ComparableType.JACKSON => new JacksonDiffTracer[T](differ, sorter)
        case ComparableType.FASTJSON => new JacksonDiffTracer[T](differ, sorter)
        case ComparableType.DOM4J => new JacksonDiffTracer[T](differ, sorter)
      }
    }

  }

  /**
    * 当传入两个[[com.fasterxml.jackson.databind.JsonNode]]时
    * 默认创建[[JacksonDiffTracer]]实例并调用其[[JacksonDiffTracer.isDifferent]]方法
    *
    * @see [[com.fasterxml.jackson.databind.JsonNode]]
    */
  def isDifferent(targetLeft: JsonNode, targetRight: JsonNode): Boolean = {
    val diffTracer = builder[Boolean]()
      .setUnitDifferTypes(DifferType.UNIT_DEFAULT)
      .setCompositeDifferTypes(DifferType.COMPOSITE_DEFAULT)
      .build

    diffTracer.asInstanceOf[JacksonDiffTracer[Boolean]].isDifferent(targetLeft, targetRight)
  }

}
