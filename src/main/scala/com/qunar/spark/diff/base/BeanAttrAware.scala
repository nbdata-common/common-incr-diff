package com.qunar.spark.diff.base

import java.lang.annotation.Annotation
import java.lang.reflect.Field

/**
  * 实现此trait后,(Element)对自己的宿主类,自己所直接映射的类及类相关属性能够有所感知
  * 一般认为,所映射的类是Plain Ordinary Java Object
  * 感知的内容包括:
  * 1. 自己的宿主类
  * 2. 自己所映射的类
  * 3. 自己在宿主类里所映射的Field
  * 4. 自己在宿主类里所拥有的所有Annotations
  */
trait BeanAttrAware {

  /**
    * 感知自己的宿主类
    */
  def hostClass[_]: Class[_]

  /**
    * 感知自己所映射的类
    */
  def selfClass[_]: Class[_]

  /**
    * 感知自己在宿主类里所映射的Field
    */
  def mappedField: Field

  /**
    * 感知自己在宿主类里所拥有的所有Annotations
    */
  def allAnnotations: Seq[Annotation] = {
    mappedField.getDeclaredAnnotations
  }

}