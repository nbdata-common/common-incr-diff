package com.qunar.spark.diff.base

import java.lang.reflect.Field

import com.google.common.cache.{CacheBuilder, CacheLoader, LoadingCache}

import scala.collection.mutable.ArrayBuffer

/**
  * java bean反射相关服务
  */
object BeanReflectService {

  // 缓存反射结果,提高反射效率
  private val reflectionCache: LoadingCache[Object, Iterable[Field]] = CacheBuilder.newBuilder()
    .maximumSize(1000)
    .build(new CacheLoader[Object, Iterable[Field]]() {
      override def load(key: Object): Iterable[Field] = {
        if (key.isInstanceOf[Class[_]]) {
          val clazz = key.asInstanceOf[Class[_]]
          clazz.getDeclaredFields
        } else {
          throw new RuntimeException("cache key does not confirm to the correct Type Class[T]")
        }
      }
    })

  private def getDeclaredFields[T](clazz: Class[T]): Iterable[Field] = {
    reflectionCache.get(clazz.getClass)
  }

  /**
    * 获得某实例的所有fields(不包括父类)
    *
    * @param instance 目标实例
    * @param clazz    该实例所对应的Class
    */
  def fetchAllFields[T](instance: Object, clazz: Class[T]): Iterable[Any] = {
    val buffer = new ArrayBuffer[Any]
    val fields = getDeclaredFields(clazz)
    for (field <- fields) {
      field.setAccessible(true)
      buffer += field.get(instance)
    }

    buffer
  }

}
