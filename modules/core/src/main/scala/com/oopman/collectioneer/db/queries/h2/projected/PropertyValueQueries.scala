package com.oopman.collectioneer.db.queries.h2.projected

import scalikejdbc.*

import java.util.UUID

object PropertyValueQueries {
  def propertyValuesByPropertyValueSets(pvsUUIDs: Seq[UUID]) =
    sql"""
      SELECT
        p.PROPERTY_NAME,
        p.PROPERTY_TYPES,
        pv.*
      FROM PROPERTIES AS p
      INNER JOIN (
          SELECT
            pvc.PROPERTY_VALUE_SET_PK,
            pvc.PROPERTY_PK,
            ARRAY_AGG(pvc.PROPERTY_VALUE) AS PVC_VALUES,
            ARRAY[] as PVI_VALUES
          FROM PROPERTY_VALUE_VARCHARS as pvc
          WHERE pvc.PROPERTY_VALUE_SET_PK IN (${pvsUUIDs})
          GROUP BY pvc.PROPERTY_VALUE_SET_PK, pvc.PROPERTY_PK
          UNION
          SELECT
            pvi.PROPERTY_VALUE_SET_PK,
            pvi.PROPERTY_PK,
            ARRAY[] as PVC_VALUES,
            ARRAY_AGG(pvi.PROPERTY_VALUE) AS PVI_VALUES
          FROM PROPERTY_VALUE_INTS as pvi
          WHERE pvi.PROPERTY_VALUE_SET_PK IN (${pvsUUIDs})
          GROUP BY pvi.PROPERTY_VALUE_SET_PK, pvi.PROPERTY_PK
      ) AS pv ON pv.PROPERTY_PK = p.PK
      ORDER BY pv.PROPERTY_VALUE_SET_PK, pv.PROPERTY_PK
       """

}
