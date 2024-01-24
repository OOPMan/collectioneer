package com.oopman.collectioneer.db.traits.dao.projected

import com.oopman.collectioneer.db.traits.entity.projected.PropertyValue
import scalikejdbc._
import java.util.UUID

trait PropertyValueDAO:

  def getPropertyValuesByPropertyValueSets(pvsUUIDs: Seq[UUID])(implicit session: DBSession = AutoSession): List[PropertyValue]

  def updatePropertyValues(propertyValues: Seq[PropertyValue])(implicit session: DBSession = AutoSession): Seq[Boolean]