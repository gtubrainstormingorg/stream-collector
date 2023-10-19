/**
  * Copyright (c) 2013-present Snowplow Analytics Ltd.
  * All rights reserved.
  *
  * This program is licensed to you under the Snowplow Community License Version 1.0,
  * and you may not use this file except in compliance with the Snowplow Community License Version 1.0.
  * You may obtain a copy of the Snowplow Community License Version 1.0 at https://docs.snowplow.io/community-license-1.0
  */
package com.snowplowanalytics.snowplow.collectors.scalastream

import cats.effect.{IO, Resource}
import com.snowplowanalytics.snowplow.collector.core.model.Sinks
import com.snowplowanalytics.snowplow.collector.core.{App, Config, Telemetry}
import com.snowplowanalytics.snowplow.collectors.scalastream.sinks._

object KafkaCollector extends App[KafkaSinkConfig](BuildInfo) {

  override def mkSinks(config: Config.Streams[KafkaSinkConfig]): Resource[IO, Sinks[IO]] =
    for {
      good <- KafkaSink.create[IO](config.good)
      bad  <- KafkaSink.create[IO](config.bad)
    } yield Sinks(good, bad)

  override def telemetryInfo(config: Config.Streams[KafkaSinkConfig]): IO[Telemetry.TelemetryInfo] =
    TelemetryUtils.getAzureSubscriptionId.map {
      case None     => Telemetry.TelemetryInfo(None, None, None)
      case Some(id) => Telemetry.TelemetryInfo(None, Some("Azure"), Some(id))
    }

}
