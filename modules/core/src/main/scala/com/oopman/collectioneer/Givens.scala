package com.oopman.collectioneer

import java.util.UUID


given stringToUUID: Conversion[String, UUID] with
  def apply(s: String): UUID = UUID.fromString(s)


