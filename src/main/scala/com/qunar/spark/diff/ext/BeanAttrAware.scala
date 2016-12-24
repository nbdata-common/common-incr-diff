package com.qunar.spark.diff.ext

import java.lang.annotation.Annotation
import java.lang.reflect.{Field, Method}

import com.google.common.base.Optional

/**
  * 实现此trait后,(Element)对自己的宿主类,自己所直接映射的类及类相关属性能够有所感知
  * 一般认为,所映射的类是Plain Ordinary Java Object
  * 感知的内容包括:
  * 1. 自己的宿主类
  * 2. 自己所映射的类
  * 3. 自己所映射的类中的所有方法
  * 4. 自己在宿主类里所映射的Field
  * 5. 自己在宿主类里所拥有的所有Annotations
  */
trait BeanAttrAware {

  val emptyAnnotationField: Field = classOf[EmptyAnnotationField].getField("emptyAnnotation")

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
  def mappedField: Optional[Field]

  /**
    * 感知自己在宿主类里所拥有的所有Annotations
    */
  def allAnnotations: Seq[Annotation] = mappedField.or(emptyAnnotationField).getDeclaredAnnotations

}

class EmptyAnnotationField {

  val emptyAnnotation: String = "field with empty annotation"

}
