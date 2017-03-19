package com.finance.share.swagger

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.finance.share.api.FinanceRestService
import com.github.swagger.akka.model.Info
import com.github.swagger.akka.{HasActorSystem, SwaggerHttpService}

import scala.reflect.runtime.{universe => ru}

/**
 * Created by Bala.
 */
class SwaggerDocService(address: String, port: Int, system: ActorSystem) extends SwaggerHttpService with HasActorSystem {
  override implicit val actorSystem: ActorSystem = system
  override implicit val materializer: ActorMaterializer = ActorMaterializer()
  override val apiTypes = Seq(ru.typeOf[FinanceRestService])
  override val host = address + ":" + port
  override val basePath = "/"
  override val apiDocsPath = "api-docs"
  override val info = Info(description = "Swagger docs for Finance service", version = "1.0", title = "Finance API", termsOfService = "Finance API terms and conditions")
}
