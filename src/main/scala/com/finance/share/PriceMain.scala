package com.finance.share

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.{ Directives, Route }
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

/**
  * Created by Bala.
  */

class PriceMain(implicit val system: ActorSystem,
                         implicit val materializer: ActorMaterializer) extends PriceController with LazyLogging {

  def startServer(asset: Route, address: String, port: Int) = {
    Http().bindAndHandle(asset, address, port)
    logger.info("Shopping Cart service has been started in the port " + port)
  }


}


object PriceMain extends App with Directives {

  println("I am in a financial price service!!!")

  implicit val actorSystem = ActorSystem("shopping-cart-rest-server")
  implicit val materializer = ActorMaterializer()

  /*val swaggerAssets =
    path("swagger-ui.html") {
      getFromResource("swagger-ui/index.html")
    } ~
      getFromResourceDirectory("swagger-ui")*/



  val config = ConfigFactory.load()
  val host = config.getStringList("http.host").get(0)
  val port = config.getIntList("http.port").get(0)

  val shoppingCartRoutes = new PriceMain().shoppingCartRoutes //~ new SwaggerDocService(host, port, actorSystem).routes ~ swaggerAssets

  val server = new PriceMain()
  server.startServer(shoppingCartRoutes, host, port)

}
