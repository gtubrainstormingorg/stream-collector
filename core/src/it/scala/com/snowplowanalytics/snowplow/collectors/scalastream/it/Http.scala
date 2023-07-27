/**
 * Copyright (c) 2013-present Snowplow Analytics Ltd.
 * All rights reserved.
 *
 * This software is made available by Snowplow Analytics, Ltd.,
 * under the terms of the Snowplow Limited Use License Agreement, Version 1.0
 * located at https://docs.snowplow.io/limited-use-license-1.0
 * BY INSTALLING, DOWNLOADING, ACCESSING, USING OR DISTRIBUTING ANY PORTION
 * OF THE SOFTWARE, YOU AGREE TO THE TERMS OF SUCH LICENSE AGREEMENT.
 */
package com.snowplowanalytics.snowplow.collectors.scalastream.it

import scala.concurrent.ExecutionContext

import cats.implicits._

import cats.effect.{ContextShift, IO, Resource}

import org.http4s.{Request, Response, Status}
import org.http4s.client.Client
import org.http4s.client.blaze.BlazeClientBuilder

object Http {

  private val executionContext = ExecutionContext.global
  implicit val ioContextShift: ContextShift[IO] = IO.contextShift(executionContext)

  def statuses(requests: List[Request[IO]]): IO[List[Status]] =
    mkClient.use { client => requests.traverse(client.status) }

  def status(request: Request[IO]): IO[Status] =
    mkClient.use { client => client.status(request) }

  def response(request: Request[IO]): IO[Response[IO]] =
    mkClient.use(c => c.run(request).use(resp => IO.pure(resp)))

  def responses(requests: List[Request[IO]]): IO[List[Response[IO]]] =
    mkClient.use(c => requests.traverse(r => c.run(r).use(resp => IO.pure(resp))))

  def mkClient: Resource[IO, Client[IO]] =
    BlazeClientBuilder[IO](executionContext).resource
}
