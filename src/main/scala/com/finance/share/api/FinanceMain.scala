package com.finance.share.api

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives
import akka.routing.FromConfig
import akka.stream.ActorMaterializer
import com.finance.share.service.{FinanceActor, FinanceCircuitBreaker}
import com.finance.share.swagger.SwaggerDocService
import com.github.swagger.akka.SwaggerSite
import com.sksamuel.elastic4s.{ElasticsearchClientUri, TcpClient}
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

/**
  * Created by Bala.
  */

class FinanceMain(implicit val system: ActorSystem, implicit val materializer: ActorMaterializer, implicit val financeActor: ActorRef) extends LazyLogging with Directives with SwaggerSite with FinanceCircuitBreaker{

  def startServer(address: String, port: Int) = {
    implicit val circuitBreaker = breakerFinance(system)
    val financeAPIRoutes = new FinanceRestService().financeRoutes ~ new SwaggerDocService(address, port, system).routes ~ swaggerSiteRoute
    Http().bindAndHandle(financeAPIRoutes, address, port)
    logger.info("Finance service has been started in the port " + port)
  }

}


object FinanceMain {

  def main(args: Array[String]): Unit = {
    val client = TcpClient.transport(ElasticsearchClientUri("localhost", 9300))
    implicit val actorSystem = ActorSystem("Finance-rest-server")
    implicit val materializer = ActorMaterializer()
    implicit val financeActor = actorSystem.actorOf(Props(new FinanceActor(client)).withRouter(FromConfig()), name = "financeActor")

    val config = ConfigFactory.load()
    val host = config.getStringList("http.host").get(0)
    val port = config.getIntList("http.port").get(0)

    val server = new FinanceMain()
    server.startServer(host, port)
  }


}
