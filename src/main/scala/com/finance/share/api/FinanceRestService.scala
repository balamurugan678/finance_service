package com.finance.share.api

import javax.ws.rs.Path

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives
import akka.pattern.ask
import akka.util.Timeout
import io.swagger.annotations._

import scala.concurrent.duration._

/**
  * Created by Bala.
  */
@Path("/finance")
@Api(value = "/finance", description = "Operations about Finance", produces = "application/json")
class FinanceRestService(implicit financeActor: ActorRef ) extends Directives{

  val financeRoutes = pathPrefix("finance") {
    financeAPIPostRoute
  }

  implicit val timeout = Timeout(5.seconds)

  import com.finance.share.domain.FinanceProtocol._

  @ApiOperation(value = "Finance API service for ElasticSearch persistence", nickname = "Finance-ElasticSearch", httpMethod = "POST", consumes = "application/json", produces = "application/json")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "GetPortfolio", dataType = "com.finance.share.domain.FinanceProtocol$PortFolio", paramType = "body", required = true)
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Bad Request"),
    new ApiResponse(code = 201, message = "Entity Created"),
    new ApiResponse(code = 500, message = "Internal Server Error")
  ))
  def financeAPIPostRoute =
    post {
      entity(as[PortFolio]) {
        portFolio => complete {
          financeActor ? portFolio
          portFolio
        }
      }
    }

}
