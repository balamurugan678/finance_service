package com.finance.share

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives
import akka.stream.ActorMaterializer

/**
  * Created by Bala.
  */
trait PriceController extends Directives {

  implicit val system: ActorSystem
  implicit val materializer: ActorMaterializer

  val shoppingCartRoutes = new PriceRestService().checkoutRoutes

}
