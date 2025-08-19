package com.oopman.collectioneer.plugins.postgresbackend.test.dao.raw

import com.oopman.collectioneer.plugins.postgresbackend.test.{BaseFunSuite, Fixtures}
import com.oopman.collectioneer.plugins.postgresbackend.dao.raw.RelationshipDAOImpl

class RawRelationshipDAOImplSpec extends BaseFunSuite:
  behavior of "com.oopman.collectioneer.plugins.postgresbackend.dao.raw.RelationshipDAOImpl.getRelationshipHierarchyByCollectionPKs"

  it should "retrieve a Seq of Relationship & HasTopLevelCollectionPKAndLevel objects" in { implicit session =>
    val fixtures = new Fixtures()
    val relationships = RelationshipDAOImpl.getRelationshipHierarchyByCollectionPKs(fixtures.rootC.pk :: Nil)
    assert(relationships.length == 7)
    for relationship <- relationships do assert(relationship.topLevelCollectionPK == fixtures.rootC.pk)
    assert(relationships.head.level == 0)
    assert(relationships.last.level == 2)
  }
