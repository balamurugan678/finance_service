package com.finance.share

import java.text.SimpleDateFormat

import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.ext.JodaTimeSerializers
import org.json4s.{ DefaultFormats, Formats, native }

/**
  * The default Json marshaller - akka-http-spray-json could have boiler-plate code for different case classes and Json4S would avoid that.
  * Adding custom serializers are easier as well
  *
  */
trait JsonSupport extends Json4sSupport {

  implicit val serialization = native.Serialization

  implicit def json4sFormats: Formats = customDateFormat ++ JodaTimeSerializers.all

  val customDateFormat = new DefaultFormats {
    override def dateFormatter = new SimpleDateFormat("yyyy-MM-dd")
  }
}
