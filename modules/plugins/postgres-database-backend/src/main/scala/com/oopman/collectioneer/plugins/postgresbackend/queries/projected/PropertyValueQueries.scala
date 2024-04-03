package com.oopman.collectioneer.plugins.postgresbackend.queries.projected

import scalikejdbc.*

import java.util.UUID

object PropertyValueQueries:

  def propertyValuesByCollectionPKs(collectionPKs: Seq[UUID], deleted: Seq[Boolean] = List(false)): SQL[Nothing, NoExtractor] =
    sql"""
      SELECT
        p.property_name,
        p.property_types,
        p.deleted,
        p.created,
        p.modified,
        pv.*
      FROM property AS p
      INNER JOIN (
          /* Varchar Property Values */
            SELECT
              collection_pk,
              property_pk,
              ARRAY_AGG(property_value) AS varchar_values,
              ARRAY[] AS varbinary_values,
              ARRAY[] AS tinyint_values,
              ARRAY[] AS smallint_values,
              ARRAY[] AS int_values,
              ARRAY[] AS bigint_values,
              ARRAY[] AS numeric_values,
              ARRAY[] AS float_values,
              ARRAY[] AS double_values,
              ARRAY[] AS boolean_values,
              ARRAY[] AS date_values,
              ARRAY[] AS time_values,
              ARRAY[] AS timestamp_values,
              ARRAY[] AS clob_values,
              ARRAY[] AS blob_values,
              ARRAY[] AS uuid_values,
              ARRAY[] AS json_values
            FROM property_value_varchar
            WHERE collection_pk IN (${collectionPKs})
            GROUP BY collection_pk, property_pk
          /* Varbinary Property Values */
          UNION
            SELECT
              collection_pk,
              property_pk,
              ARRAY[] AS varchar_values,
              ARRAY_AGG(property_value) AS varbinary_values,
              ARRAY[] AS tinyint_values,
              ARRAY[] AS smallint_values,
              ARRAY[] AS int_values,
              ARRAY[] AS bigint_values,
              ARRAY[] AS numeric_values,
              ARRAY[] AS float_values,
              ARRAY[] AS double_values,
              ARRAY[] AS boolean_values,
              ARRAY[] AS date_values,
              ARRAY[] AS time_values,
              ARRAY[] AS timestamp_values,
              ARRAY[] AS clob_values,
              ARRAY[] AS blob_values,
              ARRAY[] AS uuid_values,
              ARRAY[] AS json_values
            FROM property_value_varbinary
            WHERE collection_pk IN (${collectionPKs})
            GROUP BY collection_pk, property_pk
          /* Tinyint Property Values */
          UNION
            SELECT
              collection_pk,
              property_pk,
              ARRAY[] AS varchar_values,
              ARRAY[] AS varbinary_values,
              ARRAY_AGG(property_value) AS tinyint_values,
              ARRAY[] AS smallint_values,
              ARRAY[] AS int_values,
              ARRAY[] AS bigint_values,
              ARRAY[] AS numeric_values,
              ARRAY[] AS float_values,
              ARRAY[] AS double_values,
              ARRAY[] AS boolean_values,
              ARRAY[] AS date_values,
              ARRAY[] AS time_values,
              ARRAY[] AS timestamp_values,
              ARRAY[] AS clob_values,
              ARRAY[] AS blob_values,
              ARRAY[] AS uuid_values,
              ARRAY[] AS json_values
            FROM property_value_tinyint
            WHERE collection_pk IN (${collectionPKs})
            GROUP BY collection_pk, property_pk
          /* Smallint Property Values */
          UNION
            SELECT
              collection_pk,
              property_pk,
              ARRAY[] AS varchar_values,
              ARRAY[] AS varbinary_values,
              ARRAY[] AS tinyint_values,
              ARRAY_AGG(property_value) AS smallint_values,
              ARRAY[] AS int_values,
              ARRAY[] AS bigint_values,
              ARRAY[] AS numeric_values,
              ARRAY[] AS float_values,
              ARRAY[] AS double_values,
              ARRAY[] AS boolean_values,
              ARRAY[] AS date_values,
              ARRAY[] AS time_values,
              ARRAY[] AS timestamp_values,
              ARRAY[] AS clob_values,
              ARRAY[] AS blob_values,
              ARRAY[] AS uuid_values,
              ARRAY[] AS json_values
          FROM property_value_smallint
          WHERE collection_pk IN (${collectionPKs})
          GROUP BY collection_pk, property_pk
          /* Int Property Values */
          UNION
            SELECT
              collection_pk,
              property_pk,
              ARRAY[] AS varchar_values,
              ARRAY[] AS varbinary_values,
              ARRAY[] AS tinyint_values,
              ARRAY[] AS smallint_values,
              ARRAY_AGG(property_value) AS int_values,
              ARRAY[] AS bigint_values,
              ARRAY[] AS numeric_values,
              ARRAY[] AS float_values,
              ARRAY[] AS double_values,
              ARRAY[] AS boolean_values,
              ARRAY[] AS date_values,
              ARRAY[] AS time_values,
              ARRAY[] AS timestamp_values,
              ARRAY[] AS clob_values,
              ARRAY[] AS blob_values,
              ARRAY[] AS uuid_values,
              ARRAY[] AS json_values
            FROM property_value_int
            WHERE collection_pk IN (${collectionPKs})
            GROUP BY collection_pk, property_pk
          /* Bigint Property Values */
          UNION
            SELECT
              collection_pk,
              property_pk,
              ARRAY[] AS varchar_values,
              ARRAY[] AS varbinary_values,
              ARRAY[] AS tinyint_values,
              ARRAY[] AS smallint_values,
              ARRAY[] AS int_values,
              ARRAY_AGG(property_value) AS bigint_values,
              ARRAY[] AS numeric_values,
              ARRAY[] AS float_values,
              ARRAY[] AS double_values,
              ARRAY[] AS boolean_values,
              ARRAY[] AS date_values,
              ARRAY[] AS time_values,
              ARRAY[] AS timestamp_values,
              ARRAY[] AS clob_values,
              ARRAY[] AS blob_values,
              ARRAY[] AS uuid_values,
              ARRAY[] AS json_values
            FROM property_value_bigint
            WHERE collection_pk IN (${collectionPKs})
            GROUP BY collection_pk, property_pk
          /* Numeric Property Values */
          UNION
            SELECT
              collection_pk,
              property_pk,
              ARRAY[] AS varchar_values,
              ARRAY[] AS varbinary_values,
              ARRAY[] AS tinyint_values,
              ARRAY[] AS smallint_values,
              ARRAY[] AS int_values,
              ARRAY[] AS bigint_values,
              ARRAY_AGG(property_value) AS numeric_values,
              ARRAY[] AS float_values,
              ARRAY[] AS double_values,
              ARRAY[] AS boolean_values,
              ARRAY[] AS date_values,
              ARRAY[] AS time_values,
              ARRAY[] AS timestamp_values,
              ARRAY[] AS clob_values,
              ARRAY[] AS blob_values,
              ARRAY[] AS uuid_values,
              ARRAY[] AS json_values
            FROM property_value_numeric
            WHERE collection_pk IN (${collectionPKs})
            GROUP BY collection_pk, property_pk
          /* Float Property Values */
          UNION
            SELECT
              collection_pk,
              property_pk,
              ARRAY[] AS varchar_values,
              ARRAY[] AS varbinary_values,
              ARRAY[] AS tinyint_values,
              ARRAY[] AS smallint_values,
              ARRAY[] AS int_values,
              ARRAY[] AS bigint_values,
              ARRAY[] AS numeric_values,
              ARRAY_AGG(property_value) AS float_values,
              ARRAY[] AS double_values,
              ARRAY[] AS boolean_values,
              ARRAY[] AS date_values,
              ARRAY[] AS time_values,
              ARRAY[] AS timestamp_values,
              ARRAY[] AS clob_values,
              ARRAY[] AS blob_values,
              ARRAY[] AS uuid_values,
              ARRAY[] AS json_values
            FROM property_value_float
            WHERE collection_pk IN (${collectionPKs})
            GROUP BY collection_pk, property_pk
          /* Double Property Values */
          UNION
            SELECT
              collection_pk,
              property_pk,
              ARRAY[] AS varchar_values,
              ARRAY[] AS varbinary_values,
              ARRAY[] AS tinyint_values,
              ARRAY[] AS smallint_values,
              ARRAY[] AS int_values,
              ARRAY[] AS bigint_values,
              ARRAY[] AS numeric_values,
              ARRAY[] AS float_values,
              ARRAY_AGG(property_value) AS double_values,
              ARRAY[] AS boolean_values,
              ARRAY[] AS date_values,
              ARRAY[] AS time_values,
              ARRAY[] AS timestamp_values,
              ARRAY[] AS clob_values,
              ARRAY[] AS blob_values,
              ARRAY[] AS uuid_values,
              ARRAY[] AS json_values
            FROM property_value_double
            WHERE collection_pk IN (${collectionPKs})
            GROUP BY collection_pk, property_pk
          /* Boolean Property Values */
          UNION
            SELECT
              collection_pk,
              property_pk,
              ARRAY[] AS varchar_values,
              ARRAY[] AS varbinary_values,
              ARRAY[] AS tinyint_values,
              ARRAY[] AS smallint_values,
              ARRAY[] AS int_values,
              ARRAY[] AS bigint_values,
              ARRAY[] AS numeric_values,
              ARRAY[] AS float_values,
              ARRAY[] AS double_values,
              ARRAY_AGG(property_value) AS boolean_values,
              ARRAY[] AS date_values,
              ARRAY[] AS time_values,
              ARRAY[] AS timestamp_values,
              ARRAY[] AS clob_values,
              ARRAY[] AS blob_values,
              ARRAY[] AS uuid_values,
              ARRAY[] AS json_values
            FROM property_value_boolean
            WHERE collection_pk IN (${collectionPKs})
            GROUP BY collection_pk, property_pk
          /* Date Property Values */
          UNION
            SELECT
              collection_pk,
              property_pk,
              ARRAY[] AS varchar_values,
              ARRAY[] AS varbinary_values,
              ARRAY[] AS tinyint_values,
              ARRAY[] AS smallint_values,
              ARRAY[] AS int_values,
              ARRAY[] AS bigint_values,
              ARRAY[] AS numeric_values,
              ARRAY[] AS float_values,
              ARRAY[] AS double_values,
              ARRAY[] AS boolean_values,
              ARRAY_AGG(property_value) AS date_values,
              ARRAY[] AS time_values,
              ARRAY[] AS timestamp_values,
              ARRAY[] AS clob_values,
              ARRAY[] AS blob_values,
              ARRAY[] AS uuid_values,
              ARRAY[] AS json_values
            FROM property_value_date
            WHERE collection_pk IN (${collectionPKs})
            GROUP BY collection_pk, property_pk
          /* Time Property Values */
          UNION
            SELECT
              collection_pk,
              property_pk,
              ARRAY[] AS varchar_values,
              ARRAY[] AS varbinary_values,
              ARRAY[] AS tinyint_values,
              ARRAY[] AS smallint_values,
              ARRAY[] AS int_values,
              ARRAY[] AS bigint_values,
              ARRAY[] AS numeric_values,
              ARRAY[] AS float_values,
              ARRAY[] AS double_values,
              ARRAY[] AS boolean_values,
              ARRAY[] AS date_values,
              ARRAY_AGG(property_value) AS time_values,
              ARRAY[] AS timestamp_values,
              ARRAY[] AS clob_values,
              ARRAY[] AS blob_values,
              ARRAY[] AS uuid_values,
              ARRAY[] AS json_values
            FROM property_value_time
            WHERE collection_pk IN (${collectionPKs})
            GROUP BY collection_pk, property_pk
          /* Timestamp Property Values */
          UNION
            SELECT
              collection_pk,
              property_pk,
              ARRAY[] AS varchar_values,
              ARRAY[] AS varbinary_values,
              ARRAY[] AS tinyint_values,
              ARRAY[] AS smallint_values,
              ARRAY[] AS int_values,
              ARRAY[] AS bigint_values,
              ARRAY[] AS numeric_values,
              ARRAY[] AS float_values,
              ARRAY[] AS double_values,
              ARRAY[] AS boolean_values,
              ARRAY[] AS date_values,
              ARRAY[] AS time_values,
              ARRAY_AGG(property_value) AS timestamp_values,
              ARRAY[] AS clob_values,
              ARRAY[] AS blob_values,
              ARRAY[] AS uuid_values,
              ARRAY[] AS json_values
            FROM property_value_timestamp
            WHERE collection_pk IN (${collectionPKs})
            GROUP BY collection_pk, property_pk
          /* CLOB Property Values */
          UNION
            SELECT
              collection_pk,
              property_pk,
              ARRAY[] AS varchar_values,
              ARRAY[] AS varbinary_values,
              ARRAY[] AS tinyint_values,
              ARRAY[] AS smallint_values,
              ARRAY[] AS int_values,
              ARRAY[] AS bigint_values,
              ARRAY[] AS numeric_values,
              ARRAY[] AS float_values,
              ARRAY[] AS double_values,
              ARRAY[] AS boolean_values,
              ARRAY[] AS date_values,
              ARRAY[] AS time_values,
              ARRAY[] AS timestamp_values,
              ARRAY_AGG(property_value) AS clob_values,
              ARRAY[] AS blob_values,
              ARRAY[] AS uuid_values,
              ARRAY[] AS json_values
            FROM property_value_clob
            WHERE collection_pk IN (${collectionPKs})
            GROUP BY collection_pk, property_pk
          /* BLOB Property Values */
          UNION
            SELECT
              collection_pk,
              property_pk,
              ARRAY[] AS varchar_values,
              ARRAY[] AS varbinary_values,
              ARRAY[] AS tinyint_values,
              ARRAY[] AS smallint_values,
              ARRAY[] AS int_values,
              ARRAY[] AS bigint_values,
              ARRAY[] AS numeric_values,
              ARRAY[] AS float_values,
              ARRAY[] AS double_values,
              ARRAY[] AS boolean_values,
              ARRAY[] AS date_values,
              ARRAY[] AS time_values,
              ARRAY[] AS timestamp_values,
              ARRAY[] AS clob_values,
              ARRAY_AGG(property_value) AS blob_values,
              ARRAY[] AS uuid_values,
              ARRAY[] AS json_values
            FROM property_value_blob
            WHERE collection_pk IN (${collectionPKs})
            GROUP BY collection_pk, property_pk
          /* UUID Property Values */
          UNION
            SELECT
              collection_pk,
              property_pk,
              ARRAY[] AS varchar_values,
              ARRAY[] AS varbinary_values,
              ARRAY[] AS tinyint_values,
              ARRAY[] AS smallint_values,
              ARRAY[] AS int_values,
              ARRAY[] AS bigint_values,
              ARRAY[] AS numeric_values,
              ARRAY[] AS float_values,
              ARRAY[] AS double_values,
              ARRAY[] AS boolean_values,
              ARRAY[] AS date_values,
              ARRAY[] AS time_values,
              ARRAY[] AS timestamp_values,
              ARRAY[] AS clob_values,
              ARRAY[] AS blob_values,
              ARRAY_AGG(property_value) AS uuid_values,
              ARRAY[] AS json_values
            FROM property_value_uuid
            WHERE collection_pk IN (${collectionPKs})
            GROUP BY collection_pk, property_pk
          /* JSON Property Values */
          UNION
            SELECT
              collection_pk,
              property_pk,
              ARRAY[] AS varchar_values,
              ARRAY[] AS varbinary_values,
              ARRAY[] AS tinyint_values,
              ARRAY[] AS smallint_values,
              ARRAY[] AS int_values,
              ARRAY[] AS bigint_values,
              ARRAY[] AS numeric_values,
              ARRAY[] AS float_values,
              ARRAY[] AS double_values,
              ARRAY[] AS boolean_values,
              ARRAY[] AS date_values,
              ARRAY[] AS time_values,
              ARRAY[] AS timestamp_values,
              ARRAY[] AS clob_values,
              ARRAY[] AS blob_values,
              ARRAY[] AS uuid_values,
              ARRAY_AGG(property_value) AS json_values
            FROM property_value_json
            WHERE collection_pk IN (${collectionPKs})
            GROUP BY collection_pk, property_pk
      ) AS pv ON pv.property_pk = p.PK
      WHERE p.deleted IN (${deleted})
      ORDER BY pv.collection_pk, pv.property_pk
       """

