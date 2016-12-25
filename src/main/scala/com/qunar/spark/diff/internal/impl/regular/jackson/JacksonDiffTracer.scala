package com.qunar.spark.diff.internal.impl.regular.jackson

import com.fasterxml.jackson.databind.JsonNode
import com.qunar.spark.diff.base.compare.regular.Differ
import com.qunar.spark.diff.base.regular.elements.Element
import com.qunar.spark.diff.base.sort.Sorter
import com.qunar.spark.diff.internal.impl.regular.{AnnotatedElement, RegularDiffTracer}
import com.qunar.spark.diff.internal.impl.regular.jackson.element.JacksonNumericElement
import com.qunar.spark.diff.internal.impl.regular.jackson.json.JsonMapper

import scala.collection.mutable
import scala.reflect.ClassTag

/**
  * 适用于Jackson的incr-diff实现类
  */
private[diff] final class JacksonDiffTracer[T: ClassTag](val differ: Differ, val sorter: Sorter) extends RegularDiffTracer[T] {

  /**
    * 辅助构造器:以默认的比较器和字段排序器初始化
    */
  def this() = this(Differ(), Sorter())

  override protected val innerDiffer: Differ = differ

  override protected val innerSorter: Sorter = sorter

  // 传入的bean所对应的Class
  private val clazz = get

  private def get(implicit ct: ClassTag[T]): Class[_] = {
    ct.runtimeClass
  }

  override def isDifferent(target1: T, target2: T): Boolean = {
    val node1 = beanToJsonNode(target1)
    val node2 = beanToJsonNode(target2)
    isDifferent(node1, node2)(annotatedWrap _)
  }

  /**
    * 将java bean转为JsonNode
    */
  private def beanToJsonNode(target: T): JsonNode = {
    JsonMapper.readTree(JsonMapper.writeValueAsString(target))
  }

  def isDifferent(target1: JsonNode, target2: JsonNode)(wrapElement: (Element) => Element = direct): Boolean = {
    val node1 = jsonNodeToElement(target1, wrapElement)
    val node2 = jsonNodeToElement(target2, wrapElement)
    isDifferent(node1, node2)
  }

  private def jsonNodeToElement(node: JsonNode, wrapElement: (Element) => Element): Element = {
    // 最终要返回的目标Element
    val result: Element = null
    wrapElement(result)
    //    val stack: mutable.Stack[(JsonNode, Element)] = mutable.Stack.newBuilder[(JsonNode, Element)].+=()

    result
  }

  /**
    * 直接将元素作directly透传
    */
  private def direct(element: Element): Element = element

  /**
    * 将元素包装为注解感知的元素
    */
  private def annotatedWrap(element: Element): Element = AnnotatedElement(element, clazz)

}
