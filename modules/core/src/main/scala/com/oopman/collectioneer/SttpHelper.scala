package com.oopman.collectioneer

import sttp.client3.{Identity, RequestT, Response, SimpleHttpClient}

import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

object SttpHelper:

  @tailrec
  def sendRequest[A, B]
  (
    client: SimpleHttpClient = SimpleHttpClient(),
    request: RequestT[Identity, Either[A, B], Any],
    delayBetweenRequests: Long = 500,
    backoffFactor: Long = 1,
    backoffLimit: Int = 10
  ): Try[Response[Either[A, B]]] =
    try Success(client.send(request))
    catch case exception: Throwable =>
      val delayBeforeRetry = delayBetweenRequests * backoffFactor
      // TODO: Replace the warning below
//      logger.warn(s"Failed to retrieve ${request.uri} due to $exception. Retrying in $delayBeforeRetry milliseconds")
      this.synchronized { this.wait(delayBeforeRetry) }
      if backoffFactor < backoffLimit
      then sendRequest(client, request, delayBetweenRequests, backoffFactor + 1)
      else Failure(exception)

