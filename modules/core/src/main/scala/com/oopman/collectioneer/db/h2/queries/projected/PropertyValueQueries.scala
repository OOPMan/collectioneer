package com.oopman.collectioneer.db.h2.queries.projected

import com.oopman.collectioneer.db.traits.queries.projected.PropertyValueQueries
import scalikejdbc.*

import java.util.UUID

object PropertyValueQueries extends PropertyValueQueries:

  def propertyValuesByPropertyValueSets(pvsUUIDs: Seq[UUID], deleted: Seq[Boolean] = List(false)): SQL[Nothing, NoExtractor] =
    sql"""
      SELECT
        p.PROPERTY_NAME,
        p.PROPERTY_TYPES,
        p.DELETED,
        p.CREATED,
        p.MODIFIED,
        pv.*
      FROM PROPERTY AS p
      INNER JOIN (
          /* Varchar Property Values */
            SELECT
              PROPERTY_VALUE_SET_PK,
              PROPERTY_PK,
              ARRAY_AGG(PROPERTY_VALUE) AS VARCHAR_VALUES,
              ARRAY[] AS VARBINARY_VALUES,
              ARRAY[] AS TINYINT_VALUES,
              ARRAY[] AS SMALLINT_VALUES,
              ARRAY[] AS INT_VALUES,
              ARRAY[] AS BIGINT_VALUES,
              ARRAY[] AS NUMERIC_VALUES,
              ARRAY[] AS FLOAT_VALUES,
              ARRAY[] AS DOUBLE_VALUES,
              ARRAY[] AS BOOLEAN_VALUES,
              ARRAY[] AS DATE_VALUES,
              ARRAY[] AS TIME_VALUES,
              ARRAY[] AS TIMESTAMP_VALUES,
              ARRAY[] AS CLOB_VALUES,
              ARRAY[] AS BLOB_VALUES,
              ARRAY[] AS UUID_VALUES,
              ARRAY[] AS JSON_VALUES
            FROM PROPERTY_VALUE_VARCHAR
            WHERE PROPERTY_VALUE_SET_PK IN (${pvsUUIDs})
            GROUP BY PROPERTY_VALUE_SET_PK, PROPERTY_PK
          /* Varbinary Property Values */
          UNION
            SELECT
              PROPERTY_VALUE_SET_PK,
              PROPERTY_PK,
              ARRAY[] AS VARCHAR_VALUES,
              ARRAY_AGG(PROPERTY_VALUE) AS VARBINARY_VALUES,
              ARRAY[] AS TINYINT_VALUES,
              ARRAY[] AS SMALLINT_VALUES,
              ARRAY[] AS INT_VALUES,
              ARRAY[] AS BIGINT_VALUES,
              ARRAY[] AS NUMERIC_VALUES,
              ARRAY[] AS FLOAT_VALUES,
              ARRAY[] AS DOUBLE_VALUES,
              ARRAY[] AS BOOLEAN_VALUES,
              ARRAY[] AS DATE_VALUES,
              ARRAY[] AS TIME_VALUES,
              ARRAY[] AS TIMESTAMP_VALUES,
              ARRAY[] AS CLOB_VALUES,
              ARRAY[] AS BLOB_VALUES,
              ARRAY[] AS UUID_VALUES,
              ARRAY[] AS JSON_VALUES
            FROM PROPERTY_VALUE_VARBINARY
            WHERE PROPERTY_VALUE_SET_PK IN (${pvsUUIDs})
            GROUP BY PROPERTY_VALUE_SET_PK, PROPERTY_PK
          /* Tinyint Property Values */
          UNION
            SELECT
              PROPERTY_VALUE_SET_PK,
              PROPERTY_PK,
              ARRAY[] AS VARCHAR_VALUES,
              ARRAY[] AS VARBINARY_VALUES,
              ARRAY_AGG(PROPERTY_VALUE) AS TINYINT_VALUES,
              ARRAY[] AS SMALLINT_VALUES,
              ARRAY[] AS INT_VALUES,
              ARRAY[] AS BIGINT_VALUES,
              ARRAY[] AS NUMERIC_VALUES,
              ARRAY[] AS FLOAT_VALUES,
              ARRAY[] AS DOUBLE_VALUES,
              ARRAY[] AS BOOLEAN_VALUES,
              ARRAY[] AS DATE_VALUES,
              ARRAY[] AS TIME_VALUES,
              ARRAY[] AS TIMESTAMP_VALUES,
              ARRAY[] AS CLOB_VALUES,
              ARRAY[] AS BLOB_VALUES,
              ARRAY[] AS UUID_VALUES,
              ARRAY[] AS JSON_VALUES
            FROM PROPERTY_VALUE_TINYINT
            WHERE PROPERTY_VALUE_SET_PK IN (${pvsUUIDs})
            GROUP BY PROPERTY_VALUE_SET_PK, PROPERTY_PK
          /* Smallint Property Values */
          UNION
            SELECT
              PROPERTY_VALUE_SET_PK,
              PROPERTY_PK,
              ARRAY[] AS VARCHAR_VALUES,
              ARRAY[] AS VARBINARY_VALUES,
              ARRAY[] AS TINYINT_VALUES,
              ARRAY_AGG(PROPERTY_VALUE) AS SMALLINT_VALUES,
              ARRAY[] AS INT_VALUES,
              ARRAY[] AS BIGINT_VALUES,
              ARRAY[] AS NUMERIC_VALUES,
              ARRAY[] AS FLOAT_VALUES,
              ARRAY[] AS DOUBLE_VALUES,
              ARRAY[] AS BOOLEAN_VALUES,
              ARRAY[] AS DATE_VALUES,
              ARRAY[] AS TIME_VALUES,
              ARRAY[] AS TIMESTAMP_VALUES,
              ARRAY[] AS CLOB_VALUES,
              ARRAY[] AS BLOB_VALUES,
              ARRAY[] AS UUID_VALUES,
              ARRAY[] AS JSON_VALUES
          FROM PROPERTY_VALUE_SMALLINT
          WHERE PROPERTY_VALUE_SET_PK IN (${pvsUUIDs})
          GROUP BY PROPERTY_VALUE_SET_PK, PROPERTY_PK
          /* Int Property Values */
          UNION
            SELECT
              PROPERTY_VALUE_SET_PK,
              PROPERTY_PK,
              ARRAY[] AS VARCHAR_VALUES,
              ARRAY[] AS VARBINARY_VALUES,
              ARRAY[] AS TINYINT_VALUES,
              ARRAY[] AS SMALLINT_VALUES,
              ARRAY_AGG(PROPERTY_VALUE) AS INT_VALUES,
              ARRAY[] AS BIGINT_VALUES,
              ARRAY[] AS NUMERIC_VALUES,
              ARRAY[] AS FLOAT_VALUES,
              ARRAY[] AS DOUBLE_VALUES,
              ARRAY[] AS BOOLEAN_VALUES,
              ARRAY[] AS DATE_VALUES,
              ARRAY[] AS TIME_VALUES,
              ARRAY[] AS TIMESTAMP_VALUES,
              ARRAY[] AS CLOB_VALUES,
              ARRAY[] AS BLOB_VALUES,
              ARRAY[] AS UUID_VALUES,
              ARRAY[] AS JSON_VALUES
            FROM PROPERTY_VALUE_INT
            WHERE PROPERTY_VALUE_SET_PK IN (${pvsUUIDs})
            GROUP BY PROPERTY_VALUE_SET_PK, PROPERTY_PK
          /* Bigint Property Values */
          UNION
            SELECT
              PROPERTY_VALUE_SET_PK,
              PROPERTY_PK,
              ARRAY[] AS VARCHAR_VALUES,
              ARRAY[] AS VARBINARY_VALUES,
              ARRAY[] AS TINYINT_VALUES,
              ARRAY[] AS SMALLINT_VALUES,
              ARRAY[] AS INT_VALUES,
              ARRAY_AGG(PROPERTY_VALUE) AS BIGINT_VALUES,
              ARRAY[] AS NUMERIC_VALUES,
              ARRAY[] AS FLOAT_VALUES,
              ARRAY[] AS DOUBLE_VALUES,
              ARRAY[] AS BOOLEAN_VALUES,
              ARRAY[] AS DATE_VALUES,
              ARRAY[] AS TIME_VALUES,
              ARRAY[] AS TIMESTAMP_VALUES,
              ARRAY[] AS CLOB_VALUES,
              ARRAY[] AS BLOB_VALUES,
              ARRAY[] AS UUID_VALUES,
              ARRAY[] AS JSON_VALUES
            FROM PROPERTY_VALUE_BIGINT
            WHERE PROPERTY_VALUE_SET_PK IN (${pvsUUIDs})
            GROUP BY PROPERTY_VALUE_SET_PK, PROPERTY_PK
          /* Numeric Property Values */
          UNION
            SELECT
              PROPERTY_VALUE_SET_PK,
              PROPERTY_PK,
              ARRAY[] AS VARCHAR_VALUES,
              ARRAY[] AS VARBINARY_VALUES,
              ARRAY[] AS TINYINT_VALUES,
              ARRAY[] AS SMALLINT_VALUES,
              ARRAY[] AS INT_VALUES,
              ARRAY[] AS BIGINT_VALUES,
              ARRAY_AGG(PROPERTY_VALUE) AS NUMERIC_VALUES,
              ARRAY[] AS FLOAT_VALUES,
              ARRAY[] AS DOUBLE_VALUES,
              ARRAY[] AS BOOLEAN_VALUES,
              ARRAY[] AS DATE_VALUES,
              ARRAY[] AS TIME_VALUES,
              ARRAY[] AS TIMESTAMP_VALUES,
              ARRAY[] AS CLOB_VALUES,
              ARRAY[] AS BLOB_VALUES,
              ARRAY[] AS UUID_VALUES,
              ARRAY[] AS JSON_VALUES
            FROM PROPERTY_VALUE_NUMERIC
            WHERE PROPERTY_VALUE_SET_PK IN (${pvsUUIDs})
            GROUP BY PROPERTY_VALUE_SET_PK, PROPERTY_PK
          /* Float Property Values */
          UNION
            SELECT
              PROPERTY_VALUE_SET_PK,
              PROPERTY_PK,
              ARRAY[] AS VARCHAR_VALUES,
              ARRAY[] AS VARBINARY_VALUES,
              ARRAY[] AS TINYINT_VALUES,
              ARRAY[] AS SMALLINT_VALUES,
              ARRAY[] AS INT_VALUES,
              ARRAY[] AS BIGINT_VALUES,
              ARRAY[] AS NUMERIC_VALUES,
              ARRAY_AGG(PROPERTY_VALUE) AS FLOAT_VALUES,
              ARRAY[] AS DOUBLE_VALUES,
              ARRAY[] AS BOOLEAN_VALUES,
              ARRAY[] AS DATE_VALUES,
              ARRAY[] AS TIME_VALUES,
              ARRAY[] AS TIMESTAMP_VALUES,
              ARRAY[] AS CLOB_VALUES,
              ARRAY[] AS BLOB_VALUES,
              ARRAY[] AS UUID_VALUES,
              ARRAY[] AS JSON_VALUES
            FROM PROPERTY_VALUE_FLOAT
            WHERE PROPERTY_VALUE_SET_PK IN (${pvsUUIDs})
            GROUP BY PROPERTY_VALUE_SET_PK, PROPERTY_PK
          /* Double Property Values */
          UNION
            SELECT
              PROPERTY_VALUE_SET_PK,
              PROPERTY_PK,
              ARRAY[] AS VARCHAR_VALUES,
              ARRAY[] AS VARBINARY_VALUES,
              ARRAY[] AS TINYINT_VALUES,
              ARRAY[] AS SMALLINT_VALUES,
              ARRAY[] AS INT_VALUES,
              ARRAY[] AS BIGINT_VALUES,
              ARRAY[] AS NUMERIC_VALUES,
              ARRAY[] AS FLOAT_VALUES,
              ARRAY_AGG(PROPERTY_VALUE) AS DOUBLE_VALUES,
              ARRAY[] AS BOOLEAN_VALUES,
              ARRAY[] AS DATE_VALUES,
              ARRAY[] AS TIME_VALUES,
              ARRAY[] AS TIMESTAMP_VALUES,
              ARRAY[] AS CLOB_VALUES,
              ARRAY[] AS BLOB_VALUES,
              ARRAY[] AS UUID_VALUES,
              ARRAY[] AS JSON_VALUES
            FROM PROPERTY_VALUE_DOUBLE
            WHERE PROPERTY_VALUE_SET_PK IN (${pvsUUIDs})
            GROUP BY PROPERTY_VALUE_SET_PK, PROPERTY_PK
          /* Boolean Property Values */
          UNION
            SELECT
              PROPERTY_VALUE_SET_PK,
              PROPERTY_PK,
              ARRAY[] AS VARCHAR_VALUES,
              ARRAY[] AS VARBINARY_VALUES,
              ARRAY[] AS TINYINT_VALUES,
              ARRAY[] AS SMALLINT_VALUES,
              ARRAY[] AS INT_VALUES,
              ARRAY[] AS BIGINT_VALUES,
              ARRAY[] AS NUMERIC_VALUES,
              ARRAY[] AS FLOAT_VALUES,
              ARRAY[] AS DOUBLE_VALUES,
              ARRAY_AGG(PROPERTY_VALUE) AS BOOLEAN_VALUES,
              ARRAY[] AS DATE_VALUES,
              ARRAY[] AS TIME_VALUES,
              ARRAY[] AS TIMESTAMP_VALUES,
              ARRAY[] AS CLOB_VALUES,
              ARRAY[] AS BLOB_VALUES,
              ARRAY[] AS UUID_VALUES,
              ARRAY[] AS JSON_VALUES
            FROM PROPERTY_VALUE_BOOLEAN
            WHERE PROPERTY_VALUE_SET_PK IN (${pvsUUIDs})
            GROUP BY PROPERTY_VALUE_SET_PK, PROPERTY_PK
          /* Date Property Values */
          UNION
            SELECT
              PROPERTY_VALUE_SET_PK,
              PROPERTY_PK,
              ARRAY[] AS VARCHAR_VALUES,
              ARRAY[] AS VARBINARY_VALUES,
              ARRAY[] AS TINYINT_VALUES,
              ARRAY[] AS SMALLINT_VALUES,
              ARRAY[] AS INT_VALUES,
              ARRAY[] AS BIGINT_VALUES,
              ARRAY[] AS NUMERIC_VALUES,
              ARRAY[] AS FLOAT_VALUES,
              ARRAY[] AS DOUBLE_VALUES,
              ARRAY[] AS BOOLEAN_VALUES,
              ARRAY_AGG(PROPERTY_VALUE) AS DATE_VALUES,
              ARRAY[] AS TIME_VALUES,
              ARRAY[] AS TIMESTAMP_VALUES,
              ARRAY[] AS CLOB_VALUES,
              ARRAY[] AS BLOB_VALUES,
              ARRAY[] AS UUID_VALUES,
              ARRAY[] AS JSON_VALUES
            FROM PROPERTY_VALUE_DATE
            WHERE PROPERTY_VALUE_SET_PK IN (${pvsUUIDs})
            GROUP BY PROPERTY_VALUE_SET_PK, PROPERTY_PK
          /* Time Property Values */
          UNION
            SELECT
              PROPERTY_VALUE_SET_PK,
              PROPERTY_PK,
              ARRAY[] AS VARCHAR_VALUES,
              ARRAY[] AS VARBINARY_VALUES,
              ARRAY[] AS TINYINT_VALUES,
              ARRAY[] AS SMALLINT_VALUES,
              ARRAY[] AS INT_VALUES,
              ARRAY[] AS BIGINT_VALUES,
              ARRAY[] AS NUMERIC_VALUES,
              ARRAY[] AS FLOAT_VALUES,
              ARRAY[] AS DOUBLE_VALUES,
              ARRAY[] AS BOOLEAN_VALUES,
              ARRAY[] AS DATE_VALUES,
              ARRAY_AGG(PROPERTY_VALUE) AS TIME_VALUES,
              ARRAY[] AS TIMESTAMP_VALUES,
              ARRAY[] AS CLOB_VALUES,
              ARRAY[] AS BLOB_VALUES,
              ARRAY[] AS UUID_VALUES,
              ARRAY[] AS JSON_VALUES
            FROM PROPERTY_VALUE_TIME
            WHERE PROPERTY_VALUE_SET_PK IN (${pvsUUIDs})
            GROUP BY PROPERTY_VALUE_SET_PK, PROPERTY_PK
          /* Timestamp Property Values */
          UNION
            SELECT
              PROPERTY_VALUE_SET_PK,
              PROPERTY_PK,
              ARRAY[] AS VARCHAR_VALUES,
              ARRAY[] AS VARBINARY_VALUES,
              ARRAY[] AS TINYINT_VALUES,
              ARRAY[] AS SMALLINT_VALUES,
              ARRAY[] AS INT_VALUES,
              ARRAY[] AS BIGINT_VALUES,
              ARRAY[] AS NUMERIC_VALUES,
              ARRAY[] AS FLOAT_VALUES,
              ARRAY[] AS DOUBLE_VALUES,
              ARRAY[] AS BOOLEAN_VALUES,
              ARRAY[] AS DATE_VALUES,
              ARRAY[] AS TIME_VALUES,
              ARRAY_AGG(PROPERTY_VALUE) AS TIMESTAMP_VALUES,
              ARRAY[] AS CLOB_VALUES,
              ARRAY[] AS BLOB_VALUES,
              ARRAY[] AS UUID_VALUES,
              ARRAY[] AS JSON_VALUES
            FROM PROPERTY_VALUE_TIMESTAMP
            WHERE PROPERTY_VALUE_SET_PK IN (${pvsUUIDs})
            GROUP BY PROPERTY_VALUE_SET_PK, PROPERTY_PK
          /* CLOB Property Values */
          UNION
            SELECT
              PROPERTY_VALUE_SET_PK,
              PROPERTY_PK,
              ARRAY[] AS VARCHAR_VALUES,
              ARRAY[] AS VARBINARY_VALUES,
              ARRAY[] AS TINYINT_VALUES,
              ARRAY[] AS SMALLINT_VALUES,
              ARRAY[] AS INT_VALUES,
              ARRAY[] AS BIGINT_VALUES,
              ARRAY[] AS NUMERIC_VALUES,
              ARRAY[] AS FLOAT_VALUES,
              ARRAY[] AS DOUBLE_VALUES,
              ARRAY[] AS BOOLEAN_VALUES,
              ARRAY[] AS DATE_VALUES,
              ARRAY[] AS TIME_VALUES,
              ARRAY[] AS TIMESTAMP_VALUES,
              ARRAY_AGG(PROPERTY_VALUE) AS CLOB_VALUES,
              ARRAY[] AS BLOB_VALUES,
              ARRAY[] AS UUID_VALUES,
              ARRAY[] AS JSON_VALUES
            FROM PROPERTY_VALUE_CLOB
            WHERE PROPERTY_VALUE_SET_PK IN (${pvsUUIDs})
            GROUP BY PROPERTY_VALUE_SET_PK, PROPERTY_PK
          /* BLOB Property Values */
          UNION
            SELECT
              PROPERTY_VALUE_SET_PK,
              PROPERTY_PK,
              ARRAY[] AS VARCHAR_VALUES,
              ARRAY[] AS VARBINARY_VALUES,
              ARRAY[] AS TINYINT_VALUES,
              ARRAY[] AS SMALLINT_VALUES,
              ARRAY[] AS INT_VALUES,
              ARRAY[] AS BIGINT_VALUES,
              ARRAY[] AS NUMERIC_VALUES,
              ARRAY[] AS FLOAT_VALUES,
              ARRAY[] AS DOUBLE_VALUES,
              ARRAY[] AS BOOLEAN_VALUES,
              ARRAY[] AS DATE_VALUES,
              ARRAY[] AS TIME_VALUES,
              ARRAY[] AS TIMESTAMP_VALUES,
              ARRAY[] AS CLOB_VALUES,
              ARRAY_AGG(PROPERTY_VALUE) AS BLOB_VALUES,
              ARRAY[] AS UUID_VALUES,
              ARRAY[] AS JSON_VALUES
            FROM PROPERTY_VALUE_BLOB
            WHERE PROPERTY_VALUE_SET_PK IN (${pvsUUIDs})
            GROUP BY PROPERTY_VALUE_SET_PK, PROPERTY_PK
          /* UUID Property Values */
          UNION
            SELECT
              PROPERTY_VALUE_SET_PK,
              PROPERTY_PK,
              ARRAY[] AS VARCHAR_VALUES,
              ARRAY[] AS VARBINARY_VALUES,
              ARRAY[] AS TINYINT_VALUES,
              ARRAY[] AS SMALLINT_VALUES,
              ARRAY[] AS INT_VALUES,
              ARRAY[] AS BIGINT_VALUES,
              ARRAY[] AS NUMERIC_VALUES,
              ARRAY[] AS FLOAT_VALUES,
              ARRAY[] AS DOUBLE_VALUES,
              ARRAY[] AS BOOLEAN_VALUES,
              ARRAY[] AS DATE_VALUES,
              ARRAY[] AS TIME_VALUES,
              ARRAY[] AS TIMESTAMP_VALUES,
              ARRAY[] AS CLOB_VALUES,
              ARRAY[] AS BLOB_VALUES,
              ARRAY_AGG(PROPERTY_VALUE) AS UUID_VALUES,
              ARRAY[] AS JSON_VALUES
            FROM PROPERTY_VALUE_UUID
            WHERE PROPERTY_VALUE_SET_PK IN (${pvsUUIDs})
            GROUP BY PROPERTY_VALUE_SET_PK, PROPERTY_PK
          /* JSON Property Values */
          UNION
            SELECT
              PROPERTY_VALUE_SET_PK,
              PROPERTY_PK,
              ARRAY[] AS VARCHAR_VALUES,
              ARRAY[] AS VARBINARY_VALUES,
              ARRAY[] AS TINYINT_VALUES,
              ARRAY[] AS SMALLINT_VALUES,
              ARRAY[] AS INT_VALUES,
              ARRAY[] AS BIGINT_VALUES,
              ARRAY[] AS NUMERIC_VALUES,
              ARRAY[] AS FLOAT_VALUES,
              ARRAY[] AS DOUBLE_VALUES,
              ARRAY[] AS BOOLEAN_VALUES,
              ARRAY[] AS DATE_VALUES,
              ARRAY[] AS TIME_VALUES,
              ARRAY[] AS TIMESTAMP_VALUES,
              ARRAY[] AS CLOB_VALUES,
              ARRAY[] AS BLOB_VALUES,
              ARRAY[] AS UUID_VALUES,
              ARRAY_AGG(PROPERTY_VALUE) AS JSON_VALUES
            FROM PROPERTY_VALUE_JSON
            WHERE PROPERTY_VALUE_SET_PK IN (${pvsUUIDs})
            GROUP BY PROPERTY_VALUE_SET_PK, PROPERTY_PK
      ) AS pv ON pv.PROPERTY_PK = p.PK
      WHERE p.DELETED IN (${deleted})
      ORDER BY pv.PROPERTY_VALUE_SET_PK, pv.PROPERTY_PK
       """

