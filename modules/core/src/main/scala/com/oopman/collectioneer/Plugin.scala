package com.oopman.collectioneer

trait Plugin:
  def getName: String
  def getShortName: String = getClass.getName
  def getVersion: String
  def getRank: Int = 0
