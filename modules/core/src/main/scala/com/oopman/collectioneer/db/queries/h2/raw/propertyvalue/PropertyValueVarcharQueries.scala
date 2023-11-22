package com.oopman.collectioneer.db.queries.h2.raw.propertyvalue

import scalikejdbc._

object PropertyValueVarcharQueries:
  def insert =
    sql"""
          INSERT INTO PROPERTY_VALUE_VARCHAR(pk, property_value_set_pk, property_pk, property_value, index)
          VALUES ( ?, ?, ?, ?, ? )
       """

  def upsert =
    sql"""
          MERGE INTO PROPERTY_VALUE_VARCHAR(pk, property_value_set_pk, property_pk, property_value, index, modified)
          KEY (pk)
          VALUES (?, ?, ?, ?, ?, ?)
       """

  def deleteByPk =
    sql"""
          DELETE FROM PROPERTY_VALUE_VARCHAR
          WHERE PK IN (?)
       """

  def deleteByPropertyValueSetPksAndPropertyPks =
    sql"""
          DELETE FROM PROPERTY_VALUE_VARCHAR
          WHERE PROPERTY_VALUE_SET_PK IN (?)
          AND PROPERTY_PK IN (?)
       """
