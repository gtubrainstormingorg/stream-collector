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
package com.snowplowanalytics.snowplow.collectors.scalastream.telemetry

// iglu:com.snowplowanalytics.oss/oss_context/jsonschema/1-0-1
private case class TelemetryPayload(
  userProvidedId: Option[String]  = None,
  moduleName: Option[String]      = None,
  moduleVersion: Option[String]   = None,
  instanceId: Option[String]      = None,
  region: Option[String]          = None,
  cloud: Option[CloudVendor]      = None,
  autoGeneratedId: Option[String] = None,
  applicationName: String,
  applicationVersion: String,
  appGeneratedId: String
)
