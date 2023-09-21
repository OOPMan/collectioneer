package com.oopman.collectioneer.db.dao

import com.oopman.collectioneer.db.entity.{Collections, c}
import com.oopman.collectioneer.db.queries.h2.CollectionQueries


object CollectionsDAO {
  def getAll() =
    CollectionQueries.all.map(Collections(c.resultName)).list

}
