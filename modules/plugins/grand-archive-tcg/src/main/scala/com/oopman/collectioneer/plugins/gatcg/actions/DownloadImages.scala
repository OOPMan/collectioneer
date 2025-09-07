package com.oopman.collectioneer.plugins.gatcg.actions

import com.oopman.collectioneer.SttpHelper
import com.oopman.collectioneer.plugins.gatcg.Models
import com.typesafe.scalalogging.Logger
import io.circe.*
import io.circe.generic.auto.*
import io.circe.parser.*
import io.circe.syntax.*
import os.Path
import sttp.client3.*
import sttp.model.Uri

import java.io.ByteArrayInputStream
import scala.util.{Failure, Success, Try}

trait DownloadImages(datasetPath: Path,
                     imagesPath: Path,
                     client: SimpleHttpClient = SimpleHttpClient(),
                     baseUri: String = "https://api.gatcg.com",
                     pageSize: Int = 50,
                     delayBetweenRequests: Long = 1000):
  protected def logger: Logger
  protected var imagesDownloaded: Int = 0
  protected var failedDownloads: Int = 0
  protected var totalImages = 0

  protected def saveUriToPath(uri: Uri, path: Path): Try[Unit] =
    val request = basicRequest
      .get(uri)
      .response(asByteArray)
    if os.exists(path) then
      val errorMessage = s"$path already exists"
      logger.warn(errorMessage)
      Failure(RuntimeException(errorMessage))
    else
      logger.info(s"Saving $uri to $path")
      SttpHelper.sendRequest(client, request, delayBetweenRequests) match
        case Failure(exception) =>
          logger.error(s"Failed to download $uri due to $exception")
          Failure(exception)
        case Success(Response(Left(body), code, statusText, headers, history, request)) =>
          val errorMessage = s"Failed to retrieve $uri due to $code: $body"
          logger.error(errorMessage)
          Failure(RuntimeException(errorMessage))
        case Success(Response(Right(body), code, statusText, headers, history, request)) =>
          val inputStream = new ByteArrayInputStream(body)
          os.write(path, inputStream)
          logger.info(s"Saved $uri to $path")
          this.synchronized { this.wait(delayBetweenRequests) }
          Success(())

  protected def getAndSaveImages(images: Set[String]): Set[Try[Unit]] =
    val result = for
      image <- images
      imageSlug = image.stripPrefix("/cards/images/").stripSuffix(".jpg")
      imagePath = imagesPath / s"$imageSlug.png"
      uriString = s"$baseUri$image?rounded=true"
    yield Uri.parse(uriString) match
      case Left(value) =>
        val errorMessage = s"Failed to parse $uriString to a Uri"
        logger.error(errorMessage)
        Failure(RuntimeException(errorMessage))
      case Right(uri) =>
        val result = saveUriToPath(uri, imagePath)
        result match
          case Success(_) => imagesDownloaded += 1
          case _ => failedDownloads += 1
        result
    logger.info(s"Downloaded $imagesDownloaded images out of $totalImages")
    logger.warn(s"Failed to download $failedDownloads images out of $totalImages")
    result

  def apply(): Set[Try[Unit]] =
    if !os.exists(imagesPath) then os.makeDir.all(imagesPath)
    if !os.exists(datasetPath) then Set(Failure(new RuntimeException(s"$datasetPath does not exist")))
    else
      val json = parse(os.read(datasetPath)).getOrElse(Nil.asJson)
      import Models.*
      val cards = json.as[List[Card]].getOrElse(Nil)
      val images =
        for
          card <- cards
          edition <- card.editions
        yield
          val images =
            for innerCard <- edition.other_orientations.getOrElse(Nil)
              yield innerCard.edition.image
          edition.image +: images
      val uniqueImages = images.flatten.toSet
      imagesDownloaded = 0
      failedDownloads = 0
      totalImages = uniqueImages.size
      getAndSaveImages(uniqueImages)




