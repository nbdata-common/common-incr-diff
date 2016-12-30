package com.qunar.spark.diff.ext

import java.lang.reflect.Method

/**
  * 实现此trait后,([[com.qunar.spark.diff.base.regular.elements.Element]])对自己在宿主类里所拥有的所有[[java.lang.annotation.Annotation]]有所感知
  *
  * 针对此trait的特定功能,[[BeanAttrAware.hostClass]],[[BeanAttrAware.selfClass]],[[BeanAttrAware.allMethods]]方法都是不必要的,
  * 这里都作了默认实现:抛出[[UnsupportedOperationException]]异常,以表明此方法在本类中未实现,暂不可用
  */
trait AnnotationAware extends BeanAttrAware {

  override def hostClass: Class[_] = throw new UnsupportedOperationException("method not implemented in this Class")

  override def selfClass: Class[_] = throw new UnsupportedOperationException("method not implemented in this Class")

  override def allMethods: Seq[Method] = throw new UnsupportedOperationException("method not implemented in this Class")

}
