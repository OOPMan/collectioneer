package com.oopman.collectioneer

trait Config:
  val datasourceUri: Option[String]
  val subConfigs: Map[String, SubConfig]
