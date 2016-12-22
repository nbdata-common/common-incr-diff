package com.qunar.spark.diff.base.regular.elements.annotation.support

import java.lang.reflect.Field

import com.qunar.spark.diff.base.BeanAttrAware

/**
  *
  */
trait AnnotationAware extends BeanAttrAware {

  override def hostClass[_]: Class[_] = throw new UnsupportedOperationException("method not implemented under this Class")

  override def selfClass[_]: Class[_] = throw new UnsupportedOperationException("method not implemented under this Class")

  override def mappedField: Field

}
