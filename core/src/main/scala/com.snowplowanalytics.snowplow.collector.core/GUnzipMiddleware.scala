/**
  * Copyright (c) 2013-present Snowplow Analytics Ltd.
  * All rights reserved.
  *
  * This program is licensed to you under the Snowplow Community License Version 1.0,
  * and you may not use this file except in compliance with the Snowplow Community License Version 1.0.
  * You may obtain a copy of the Snowplow Community License Version 1.0 at https://docs.snowplow.io/community-license-1.0
  */
package com.snowplowanalytics.snowplow.collector.core

import fs2.compression.Compression
import cats.data.Kleisli
import cats.effect._
import org.http4s._
import org.http4s.headers.`Content-Encoding`

object GUnzipMiddleware {
  def apply[F[_]: Sync](service: HttpRoutes[F], bufferSize: Int = 100 * 1024): HttpRoutes[F] =
    Kleisli { (req: Request[F]) =>
      val req2 = req.headers.get[`Content-Encoding`] match {
        case Some(header) if satisfiedByGzip(header) =>
          val decoded = req.body.through(Compression.forSync[F].gunzip(bufferSize)).flatMap(_.content).handleErrorWith {
            e =>
              throw MalformedMessageBodyFailure(
                "Failed to decode gzippped request body",
                Some(e)
              )
          }
          req
            .removeHeader[`Content-Encoding`]
            .withEntity(decoded)(
              EntityEncoder.entityBodyEncoder
            ) // resolving implicit conflict
        case _ => req
      }
      service(req2)
    }

  private def satisfiedByGzip(header: `Content-Encoding`) =
    header.contentCoding.matches(ContentCoding.gzip) || header.contentCoding.matches(ContentCoding.`x-gzip`)
}
