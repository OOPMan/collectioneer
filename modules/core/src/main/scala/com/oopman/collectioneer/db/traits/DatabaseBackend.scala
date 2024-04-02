package com.oopman.collectioneer.db.traits

import com.oopman.collectioneer.db.traits

trait DatabaseBackend:
  // TODO: We probably need to remove this
  val migrationLocations: Seq[String]
  val queries: traits.queries.QueryObjects
  val dao: traits.dao.DAOObjects
