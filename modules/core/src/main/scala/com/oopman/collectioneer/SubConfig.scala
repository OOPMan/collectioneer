package com.oopman.collectioneer

import distage.ModuleDef
import izumi.reflect.Tag
import izumi.reflect.macrortti.LightTypeTag

trait SubConfig:
  private val key = getClass.getSimpleName.stripSuffix("SubConfig").toLowerCase
  final def getKeyForSubConfig: String = key
  
  def getTag: LightTypeTag
  def getModuleDefForSubConfig: ModuleDef
  
trait WithTag[T: Tag]:
  private val tag = Tag[T].tag
  def getTag: LightTypeTag = tag
