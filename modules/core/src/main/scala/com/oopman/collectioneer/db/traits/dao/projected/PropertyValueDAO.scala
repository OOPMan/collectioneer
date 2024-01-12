package com.oopman.collectioneer.db.traits.dao.projected

import scalikejdbc._
import java.util.UUID
import com.oopman.collectioneer.db.entity.projected.PropertyValue

trait PropertyValueDAO:

  def getPropertyValuesByPropertyValueSets(pvsUUIDs: Seq[UUID])(implicit session: DBSession = AutoSession): List[PropertyValue]