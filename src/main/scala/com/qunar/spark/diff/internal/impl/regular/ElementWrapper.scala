package com.qunar.spark.diff.internal.impl.regular

import com.qunar.spark.diff.base.regular.elements.Element
import com.qunar.spark.diff.internal.impl.WRAPPER

import scala.collection.mutable.ArrayBuffer
import scala.reflect.ClassTag

/**
  * 针对递归结构的[[Element]]包装器工具类
  * 提供各种包装函数以及构建复合包装函数的[[WrapperBuilder]]
  */
private[regular] class ElementWrapper[T: ClassTag] {

  /* 以下几个常量为包装方法的函数化表示,方便外部直接使用 */

  /**
    * [[direct]]方法的函数化
    */
  val DIRECT = direct _

  /**
    * [[annotatedWrap]]方法的函数化
    */
  val ANNOTATED_WRAPPER = annotatedWrap _


  /**
    * 返回一个[[WrapperBuilder]]用于定制化构造自己的[[WRAPPER]]
    */
  def wrapBuilder: WrapperBuilder = new WrapperBuilder

  /**
    * 用于构造[[WRAPPER]]包装函数链,可以自己customize将哪些[[WRAPPER]]加入至链中
    * <p/>
    * for example: <pre> {{{
    *
    * val wrapper = ElementWrapper.wrapBuilder
    *   .addWrapper(ElementWrapper.ANNOTATED_WRAPPER)
    *   .addWrapper(ElementWrapper.DIRECT)
    *   .build
    *
    * }}} </pre>
    */
  class WrapperBuilder {

    // 用于构建函数链的内部builder
    private val wrappersBuilder = ArrayBuffer.newBuilder[WRAPPER]
    // 容量预加载
    wrappersBuilder.sizeHint(3)

    /**
      * 添加一个[[WRAPPER]]至包装函数链
      */
    def addWrapper(wrapper: WRAPPER): this.type = {
      wrappersBuilder += wrapper
      this
    }

    /**
      * [[addWrapper]]方法的可变参数版本
      */
    def addWrappers(wrappers: WRAPPER*): this.type = {
      wrappersBuilder ++= wrappers
      this
    }

    /**
      * 实际构建[[WRAPPER]]链的过程:
      * 内部定义一个符合[[WRAPPER]]输入输出特性的方法innerWrap,并将[[wrappersBuilder]]
      * 中的wrappers的输入输出按顺序连接起来构成[[WRAPPER]]链.
      * 最终返回该内部方法innerWrap的函数实例.
      */
    def build: WRAPPER = {
      val wrappers = wrappersBuilder.result
      def innerWrap(element: Element): Element = {
        var result: Element = element
        for (wrapper <- wrappers) {
          result = wrapper(element)
        }
        result
      }

      innerWrap _
    }

  }

  /* 以上为Element包装函数常量及public api,以下为具体实现逻辑 */

  // 传入的bean所对应的Class
  protected val hostClazz = getClassT

  private def getClassT(implicit ct: ClassTag[T]): Class[_] = {
    ct.runtimeClass
  }

  /**
    * 直接将元素作directly透传
    */
  private def direct(element: Element): Element = element

  /**
    * 将元素包装为注解感知([[com.qunar.spark.diff.ext.AnnotationAware]])的元素
    */
  private def annotatedWrap(element: Element): Element = AnnotatedElement(element, hostClazz)

}
