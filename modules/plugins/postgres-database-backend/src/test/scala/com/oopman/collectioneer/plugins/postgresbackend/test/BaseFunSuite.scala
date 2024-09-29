package com.oopman.collectioneer.plugins.postgresbackend.test

import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.FixtureAnyFlatSpec
import scalikejdbc.scalatest.AutoRollback

class BaseFunSuite extends FixtureAnyFlatSpec with AutoRollback with BeforeAndAfterAll
  // TODO Add fixture stuff for database
