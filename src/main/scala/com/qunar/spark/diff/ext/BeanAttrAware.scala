package com.qunar.spark.diff.ext

import java.lang.annotation.Annotation
import java.lang.reflect.{Field, Method}

/**
  * 实现此trait后,([[com.qunar.spark.diff.base.regular.elements.Element]])对自己的宿主类,自己所直接映射的类及类相关属性能够有所感知
  * 一般认为,所映射的类是Plain Ordinary Java Object
  * 感知的内容包括:
  * 1. 自己的宿主[[Class]]
  * 2. 自己所映射的[[Class]]
  * 3. 自己所映射的类中的所有[[Method]]
  * 4. 自己在宿主类里所映射的[[Field]]
  * 5. 自己在宿主类里所拥有的所有[[Annotation]]
  */
trait BeanAttrAware {

  // 懒加载一个不携带任何注解的Field
  lazy val emptyAnnotationField: Field = classOf[EmptyAnnotationField].getField("emptyAnnotation")

  /**
    * 感知自己的宿主类
    */
  def hostClass: Class[_]

  /**
    * 感知自己所映射的类
    */
  def selfClass: Class[_]

  /**
    * 感知自己所映射的类中的所有方法
    */
  def allMethods: Seq[Method]

  /**
    * 感知自己在宿主类里所映射的Field
    */
  def mappedField: Option[Field]

  /**
    * 感知自己在宿主类里所拥有的所有[[Annotation]]
    *
    * NOTICE: 如果[[mappedField]]方法返回的[[Option]]是[[None]]类型,则此方法默认返回空序列
    */
  def allAnnotations: Seq[Annotation] = mappedField.getOrElse(emptyAnnotationField).getDeclaredAnnotations

}

/**
  * 构造一个类,此类只有一个字段,且该字段不携带任何注解,用于获取一个空的[[Seq<Annotation>]]
  */
class EmptyAnnotationField {

  // 该String类型的字段不带任何注解
  val emptyAnnotation: String = "field with empty annotation"

}
