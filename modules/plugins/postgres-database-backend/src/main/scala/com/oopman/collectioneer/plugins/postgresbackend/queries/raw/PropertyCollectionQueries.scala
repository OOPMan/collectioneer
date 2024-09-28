package com.oopman.collectioneer.plugins.postgresbackend.queries.raw

import com.oopman.collectioneer.plugins.postgresbackend.entity.raw.PropertyCollection
import scalikejdbc.*

object PropertyCollectionQueries:
  def insert =
    sql"""
          INSERT INTO property_collection(property_pk, collection_pk, index, property_collection_relationship_type)
          VALUES (?, ?, ?, cast(? AS property_collection_relationship_type))
       """
  // TODO: Upsert should probably not change the index value
  def upsert =
    sql"""
          INSERT INTO property_collection(property_pk, collection_pk, index, property_collection_relationship_type)
          VALUES (?, ?, ?, cast(? AS property_collection_relationship_type))
          ON CONFLICT(property_pk, collection_pk) DO UPDATE
          SET index = excluded.index, property_collection_relationship_type = cast(excluded.property_collection_relationship_type AS property_collection_relationship_type) ,
          modified = now()
       """
    
  def getAllMatchingCollectionPKs =
    sql"""
          SELECT ${PropertyCollection.pc1.result.*}
          FROM ${PropertyCollection.as(PropertyCollection.pc1)}
          WHERE ${PropertyCollection.pc1.collectionPK} = ANY (?::uuid[])
       """
  
