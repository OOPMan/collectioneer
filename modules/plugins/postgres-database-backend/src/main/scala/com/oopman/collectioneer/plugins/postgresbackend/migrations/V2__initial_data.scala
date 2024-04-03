package com.oopman.collectioneer.plugins.postgresbackend.migrations

import org.flywaydb.core.api.migration.{BaseJavaMigration, Context}

/**
 *
 */
class V2__initial_data extends BaseJavaMigration:
//  def executeMigration(DAOs: dao.DAOs): Unit =
//    val collectionDAO = DAOs.projected.collectionDAO
//    collectionDAO.createCollections(CoreCollections.values.map(_.collection))
//    val propertyDAO = DAOs.raw.propertyDAO
//    val propertyValueSetDAO = DAOs.raw.propertyValueSetDAO
//    val propertyPropertyValueSetDAO = DAOs.raw.propertyPropertyValueSetDAO
//    val collectionDAO = DAOs.raw.collectionDAO
//    val propertyValueDAO = DAOs.projected.propertyValueDAO
//    // Insert Property rows
//    propertyDAO.createProperties(CoreProperties.values.toList.map(_.property))
//    // Insert PropertyValueSet row
//    val propertyValueSets =
//      CoreProperties.values.flatMap(_.property.propertyValueSets) ++
//      CoreCollections.values.flatMap(_.collection.propertyValueSets)
//    propertyValueSetDAO.createPropertyValueSets(propertyValueSets)
//    // Insert Collection rows
//    collectionDAO.createCollections(CoreCollections.values.toList.map(_.collection))
//    // Associate Properties with PropertyValueSets
//    val propertyPropertyValueSets = CoreCollections.values.flatMap(_.collection.propertyValueSets.flatMap(pvs =>
//      pvs.properties.map(p => entity.raw.PropertyPropertyValueSet(propertyPK = p.pk, propertyValueSetPK = pvs.pk)) ++
//      pvs.propertyValues.map(pv => entity.raw.PropertyPropertyValueSet(propertyPK = pv.property.pk, propertyValueSetPK = pvs.pk))
//    )).distinct // TODO: PropertyPropertyValueSets associated with Properties too (remember relationship will be different tho)
//    propertyPropertyValueSetDAO.createOrUpdatePropertyPropertyValueSets(propertyPropertyValueSets)
//    // Save PropertyValues
//    // TODO: PropertyValues associated with Properties too
//    // TODO: PropertyValues on CoreCollections don't define full structure (I.e. PVS on PV is default generated)
//    // TODO: fix this by either defining those properly or developling a proper project CollectionDAO that can write the whole structure
//    val propertyValues = CoreCollections.values.flatMap(_.collection.propertyValueSets.flatMap(_.propertyValues))
//    propertyValueDAO.updatePropertyValues(propertyValues)

  override def canExecuteInTransaction: Boolean = false
  override def migrate(context: Context): Unit = ???
    /*
    val connection = context.getConnection
    Injection.produceRun(connection, false)(executeMigration)

     */
