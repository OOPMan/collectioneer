package com.oopman.collectioneer.plugins.postgresbackend.test.dao.projected

import com.oopman.collectioneer.plugins.postgresbackend.dao.projected.PropertyValueDAOImpl
import com.oopman.collectioneer.plugins.postgresbackend.test.{BaseFunSuite, Fixtures}

class ProjectedPropertyValueDAOImplSpec extends BaseFunSuite {
  behavior of "com.oopman.collectioneer.plugins.postgresbackend.dao.projected.PropertyValueDAOImpl.getPropertyValuesByCollectionUUIDs"

  it should "return a List of PropertyValue objects for the provided Collections" in { implicit session =>
    val fixtures = new Fixtures()
    import fixtures._
    val propertyValues = PropertyValueDAOImpl.getPropertyValuesByCollectionUUIDs(Seq(childAofRootA.pk))
  }

}
