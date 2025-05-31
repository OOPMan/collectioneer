package com.oopman.collectioneer

import distage.ModuleDef
import izumi.reflect.*

trait ConfigManager:
  final def getSubConfig[T <: SubConfig : Tag]: Option[T] =
    getConfig.subConfigs.values.find(_.getTag <:< Tag[T].tag).asInstanceOf[Option[T]]

  final def getModuleDefForSubConfig[T <: SubConfig : Tag]: Option[ModuleDef] =
    getSubConfig[T].map(_.getModuleDefForSubConfig)

  def getConfig: Config
  def updateConfig(config: Config): Config
  def getModuleDefForConfig: ModuleDef
  def updateSubConfig[T <: SubConfig](subConfig: T): T
