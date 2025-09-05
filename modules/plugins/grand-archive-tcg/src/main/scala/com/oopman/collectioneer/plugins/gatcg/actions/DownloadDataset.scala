package com.oopman.collectioneer.plugins.gatcg.actions

import com.oopman.collectioneer.SttpHelper
import com.typesafe.scalalogging.Logger
import io.circe.*
import io.circe.optics.JsonPath.*
import io.circe.syntax.*
import os.Path
import sttp.client3.circe.*
import sttp.client3.*

import scala.util.{Failure, Success, Try}

trait DownloadDataset(path: Path,
                      client: SimpleHttpClient = SimpleHttpClient(),
                      baseUri: String = "https://api.gatcg.com",
                      pageSize: Int = 50,
                      delayBetweenRequests: Long = 500):
  protected def logger: Logger
  protected var totalPages: Option[Int] = None

  protected def getData(page: Int = 1): Try[Vector[Json]] =
    val uri = uri"$baseUri/cards/search?page=$page&page_size=$pageSize"
    val request = basicRequest
      .get(uri)
      .response(asJson[io.circe.Json])
    val pageCount = totalPages.map(totalPages => s"of $totalPages ").getOrElse("")
    logger.info(s"Downloading page $page ${pageCount}with page size $pageSize from $uri")
    SttpHelper.sendRequest(client, request, delayBetweenRequests) match
      case Success(Response(Left(body), code, statusText, headers, history, request)) =>
        val message = s"Error downloading $page with page size $pageSize from $baseUri: $code"
        logger.error(message)
        Failure(RuntimeException(message))
      case Success(Response(Right(body), code, statusText, headers, history, request)) =>
        val hasMore = root.has_more.boolean.getOption(body).getOrElse(false)
        val data = root.data.arr.getOption(body).getOrElse(Vector())
        totalPages = root.total_pages.int.getOption(body)
        if !hasMore then
          Success(data)
        else
          this.synchronized { wait(delayBetweenRequests) }
          getData(page + 1).map(newData => data :++ newData)
      case Failure(exception) => Failure(exception)

  def apply(): Try[Vector[Json]] =
    logger.info("Downloading GATCG dataaset")
    val result = getData() match
      case success @ Success(data) =>
        logger.info(s"Downloaded ${data.length} GATCG cards")
        val dataAsString = data.asJson.spaces2
        os.write(path, dataAsString)
        logger.info(s"Wrote GATCG dataset to $path")
        success
      case failure @ Failure(e) => failure
    result