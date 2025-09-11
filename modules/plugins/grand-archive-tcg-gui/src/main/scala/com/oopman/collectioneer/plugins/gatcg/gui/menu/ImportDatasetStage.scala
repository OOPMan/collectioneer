package com.oopman.collectioneer.plugins.gatcg.gui.menu

import com.oopman.collectioneer.Injection
import com.oopman.collectioneer.db.entity.projected.Collection
import com.oopman.collectioneer.db.entity.raw.Relationship
import com.oopman.collectioneer.db.traits
import com.oopman.collectioneer.db.traits.entity.projected.Property
import com.oopman.collectioneer.plugins.gatcg.actions.ImportDataset
import com.typesafe.scalalogging.LazyLogging
import javafx.concurrent.Task
import scalafx.stage.Stage

import java.io.File
import scala.util.{Success, Try}

object ImportDatasetStage:
  def apply(datasetPath: File, parent: Stage) = new ImportDatasetStage(datasetPath, parent)

class ImportDatasetStage(datasetPath: File, parent: Stage) extends AbstractActionStage(parent):
  title = "Import GATCG Dataset"

  private trait DAOs:
    val collectionDAO: traits.dao.projected.CollectionDAO
    val relationshipDAO: traits.dao.raw.RelationshipDAO
    val propertyDAO: traits.dao.projected.PropertyDAO

  private val daos = Try:
    new DAOs:
      val collectionDAO = Injection.produce[traits.dao.projected.CollectionDAO]()
      val relationshipDAO = Injection.produce[traits.dao.raw.RelationshipDAO]()
      val propertyDAO = Injection.produce[traits.dao.projected.PropertyDAO]()

  protected val task =
    new Task[Try[Int]] with ImportDataset(os.Path(datasetPath)) with LazyLogging:
      var totalCollectionsAndRelationships = 0
      var collectionsWritten = 0
      var relationshipsWritten = 0

      override def call(): Try[Int] =
        configureLogger(logger)
        daos
          .map(daos => apply())
          .recover { exception =>
            logger.error("Unable to obtain connection to Database")
            0
          }

      override protected def writeCollectionsAndRelationships(collections: Seq[Collection], relationships: Seq[Relationship]): Int =
        totalCollectionsAndRelationships = collections.size + relationships.size
        super.writeCollectionsAndRelationships(collections, relationships)

      override protected def writeCollections(collections: Seq[Collection]): Seq[Int] =
        val result =
          if isCancelled then Success(Nil)
          else for daos <- daos yield
            val result = daos.collectionDAO.createOrUpdateCollections(collections)
            collectionsWritten += collections.size
            updateProgress(collectionsWritten + relationshipsWritten, totalCollectionsAndRelationships)
            result
        result.getOrElse(Nil)

      override protected def writeRelationships(relationships: Seq[Relationship]): Seq[Int] =
        val result =
          if isCancelled then Success(Nil)
          else for daos <- daos yield
              val result = daos.relationshipDAO.createOrUpdateRelationships(relationships)
              relationshipsWritten += relationships.size
              updateProgress(collectionsWritten + relationshipsWritten, totalCollectionsAndRelationships)
              result
        result.getOrElse(Nil)

      override protected def writeProperties(properties: Seq[Property]): Seq[Int] =
        if isCancelled then Nil
        else daos.map(daos => daos.propertyDAO.createOrUpdateProperties(properties)).getOrElse(Nil)
