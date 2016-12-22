package com.qunar.spark.diff.base.regular.elements.annotation.support

import com.qunar.spark.diff.base.BeanAttrAware

/**
  * 实现此trait后,(Element)对自己在宿主类里所拥有的所有Annotations有所感知
  *
  * 针对此trait的特定功能,hostClass方法与selfClass方法都是不必要的,
  * 这里都作了默认实现:抛出UnsupportedOperationException异常,以表明此方法在本类中不可用
  */
trait AnnotationAware extends BeanAttrAware {

  override def hostClass[_]: Class[_] = throw new UnsupportedOperationException("method not implemented in this Class")

  override def selfClass[_]: Class[_] = throw new UnsupportedOperationException("method not implemented in this Class")

}
