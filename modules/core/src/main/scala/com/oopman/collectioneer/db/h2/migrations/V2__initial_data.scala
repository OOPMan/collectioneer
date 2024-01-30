package com.oopman.collectioneer.db.h2.migrations

import com.oopman.collectioneer.db.traits.entity.raw
import com.oopman.collectioneer.db.traits.entity.raw.PropertyCollectionRelationship
import com.oopman.collectioneer.db.{Injection, dao, entity, h2, traits}
import com.oopman.collectioneer.{CoreCollections, CoreProperties}
import org.flywaydb.core.api.migration.{BaseJavaMigration, Context}
import distage.*
import izumi.fundamentals.platform.functional.Identity

/**
 *
 */
class V2__initial_data extends BaseJavaMigration:
  def executeMigration(DAOs: dao.DAOs): Unit =
    val propertyDAO = DAOs.raw.propertyDAO
    val propertyCollectionDAO = DAOs.raw.propertyCollectionDAO
    val propertyValueSetDAO = DAOs.raw.propertyValueSetDAO
    val collectionDAO = DAOs.raw.collectionDAO
    val propertyValueDAO = DAOs.projected.propertyValueDAO
    // Insert Property rows
    propertyDAO.createProperties(CoreProperties.values.toList.map(_.property))
    // Insert PropertyValueSet row
    propertyValueSetDAO.createPropertyValueSets(
      CoreProperties.values.map(p => entity.raw.PropertyValueSet(pk = p.property.pk)) ++
        CoreCollections.values.map(c => entity.raw.PropertyValueSet(pk = c.collection.pk))
    )
    // Insert Collection rows
    collectionDAO.createCollections(CoreCollections.values.toList.map(_.collection))
    // Associate Properties with Collections
    val propertiesOfCollections = CoreCollections.values.flatMap(
      c => c.collection.properties.map(
        p => entity.raw.PropertyCollection(
          propertyPK = p.pk,
          collectionPK = c.collection.pk,
          propertyValueSetPK = c.collection.pk
        )
      )
    ).toList
    // Associate commonPropertiesOfProperties Collection with name and description Core Properties
    val propertiesOfProperties = List(
      entity.raw.PropertyCollection(
        propertyPK = CoreProperties.name.property.pk,
        collectionPK = CoreCollections.commonPropertiesOfProperties.collection.pk,
        propertyValueSetPK = CoreProperties.name.property.pk,
        relationship = PropertyCollectionRelationship.CollectionOfPropertiesOfProperty
      ),
      entity.raw.PropertyCollection(
        propertyPK = CoreProperties.description.property.pk,
        collectionPK = CoreCollections.commonPropertiesOfProperties.collection.pk,
        propertyValueSetPK = CoreProperties.description.property.pk,
        relationship = raw.PropertyCollectionRelationship.CollectionOfPropertiesOfProperty,
      )
    )
    // Save associations
    propertyCollectionDAO.createPropertyCollections(
      propertiesOfCollections ++ propertiesOfProperties
    )
    // Save PropertyValues
    propertyValueDAO.updatePropertyValues(CoreCollections.values.flatMap(_.collection.propertyValues))

  override def canExecuteInTransaction: Boolean = false
  override def migrate(context: Context): Unit =
    val connection = context.getConnection
    Injection.produceRun(connection, false)(executeMigration)
    /*
    val propertyDAO = new PropertyDAO(() => db)
    val collectionDAO = new CollectionDAO(() => db)
    val propertyValueSetDAO = new PropertyValueSetDAO(() => db)
    val propertyCollectionDAO = new PropertyCollectionDAO(() => db)
    // Insert Property rows
    propertyDAO.createProperties(CoreProperties.values.toList.map(_.property))
    // Insert PropertyValueSet row
    propertyValueSetDAO.createPropertyValueSets(
      CoreProperties.values.map(p => entity.raw.PropertyValueSet(pk = p.property.pk)) ++
      CoreCollections.values.map(c => entity.raw.PropertyValueSet(pk = c.collection.pk))
    )
    // Insert Collection rows
    collectionDAO.createCollections(CoreCollections.values.toList.map(_.collection))
    // Associate Properties with Collections
    val propertiesOfCollections = CoreCollections.values.flatMap(
      c => c.collection.properties.map(
        p => entity.raw.PropertyCollection(
          propertyPK = p.pk,
          collectionPK = c.collection.pk,
          propertyValueSetPK = c.collection.pk
        )
      )
    ).toList
    // Associate commonPropertiesOfProperties Collection with name and description Core Properties
    val propertiesOfProperties = List(
      entity.raw.PropertyCollection(
        propertyPK = CoreProperties.name.property.pk,
        collectionPK = CoreCollections.commonPropertiesOfProperties.collection.pk,
        propertyValueSetPK = CoreProperties.name.property.pk,
        relationship = PropertyCollectionRelationship.CollectionOfPropertiesOfProperty
      ),
      entity.raw.PropertyCollection(
        propertyPK = CoreProperties.description.property.pk,
        collectionPK = CoreCollections.commonPropertiesOfProperties.collection.pk,
        propertyValueSetPK = CoreProperties.description.property.pk,
        relationship = traits.entity.PropertyCollectionRelationship.CollectionOfPropertiesOfProperty,
      )
    )
    // Save associations
    propertyCollectionDAO.createPropertyCollections(
      propertiesOfCollections ++ propertiesOfProperties
    )

     */
//    // TODO: Insert PropertyValues for the properties of name and description to set in place sensible defaults
//    val insertPropertyValueVarchars = connection.prepareStatement(
//      "INSERT INTO property_value_varchar(property_value_set_pk, property_pk, property_value) VALUES (?, ?, ?)"
//    )
//    val insertPropertyValueInts = connection.prepareStatement(
//      "INSERT INTO property_value_int(property_value_set_pk, property_pk, property_value) VALUES (?, ?, ?)"
//    )
//    // CommonProperties.name
//    insertPropertyValueVarchars.setString(1, CoreCollections.CommonProperties.uuid.toString)
//    insertPropertyValueVarchars.setString(2, CoreProperties.name.uuid.toString)
//    insertPropertyValueVarchars.setString(3, "Common Properties")
//    insertPropertyValueVarchars.addBatch()
//    // CommonProperties.name.min_values
//    insertPropertyValueInts.setString(1, namePropertyValueSetPk)
//    insertPropertyValueInts.setString(2, CoreProperties.min_values.uuid.toString)
//    insertPropertyValueInts.setInt(3, 1)
//    insertPropertyValueInts.addBatch()
//    // CommonProperties.name.max_values
//    insertPropertyValueInts.setString(1, namePropertyValueSetPk)
//    insertPropertyValueInts.setString(2, CoreProperties.max_values.uuid.toString)
//    insertPropertyValueInts.setInt(3, Int.MaxValue)
//    insertPropertyValueInts.addBatch()
//    // CommonProperties.description
//    insertPropertyValueVarchars.setString(1, CoreCollections.CommonProperties.uuid.toString)
//    insertPropertyValueVarchars.setString(2, CoreProperties.description.uuid.toString)
//    insertPropertyValueVarchars.setString(3, "A Collection of Properties automatically available to all other Collections")
//    insertPropertyValueVarchars.addBatch()
//    // CommonProperties.description.min_values
//    insertPropertyValueInts.setString(1, descriptionPropertyValueSetPk)
//    insertPropertyValueInts.setString(2, CoreProperties.min_values.uuid.toString)
//    insertPropertyValueInts.setInt(3, 0)
//    insertPropertyValueInts.addBatch()
//    // CommonProperties.description.max_values
//    insertPropertyValueInts.setString(1, descriptionPropertyValueSetPk)
//    insertPropertyValueInts.setString(2, CoreProperties.max_values.uuid.toString)
//    insertPropertyValueInts.setInt(3, Int.MaxValue)
//    insertPropertyValueInts.addBatch()
//    // Execute batch
//    insertPropertyValueVarchars.executeBatch()
//    insertPropertyValueInts.executeBatch()
