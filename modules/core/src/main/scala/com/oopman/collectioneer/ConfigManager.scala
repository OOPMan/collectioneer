package com.oopman.collectioneer

import distage.ModuleDef

trait ConfigManager:
  def getConfig: Config
  def getModuleDefForConfig: ModuleDef
