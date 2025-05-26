package com.oopman.collectioneer

import distage.ModuleDef

trait SubConfig:
  def getModuleDefForSubConfig: ModuleDef
