package com.finance.share.api

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Directives
import akka.stream.ActorMaterializer

/**
  * Created by Bala.
  */
trait FinanceController extends Directives {

  implicit val system: ActorSystem
  implicit val materializer: ActorMaterializer
  implicit val financeActor: ActorRef

  val financeRoutes = new FinanceRestService().financeRoutes

}
