package com.finance.share.api

import javax.ws.rs.Path

import akka.actor.ActorRef
import akka.http.scaladsl.server.{Directives, ValidationRejection}
import akka.pattern.{CircuitBreaker, ask}
import akka.util.Timeout
import io.swagger.annotations._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

/**
  * Created by Bala.
  */
@Path("/finance")
@Api(value = "/finance", description = "Operations about Finance", produces = "application/json")
class FinanceRestService(implicit financeActor: ActorRef, implicit val breaker: CircuitBreaker) extends Directives {

  val financeRoutes = pathPrefix("finance") {
    financeAPIPostRoute
  }

  implicit val timeout = Timeout(15.seconds)

  import com.finance.share.domain.FinanceProtocol._

  @ApiOperation(value = "Finance API service for ElasticSearch persistence", nickname = "Finance-FSA", httpMethod = "POST", consumes = "application/json", produces = "application/json")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "CreatePortfolio", dataType = "com.finance.share.domain.FinanceProtocol$PortFolio", paramType = "body", required = true),
    new ApiImplicitParam(name = "cid", dataType = "java.lang.String", paramType = "header", required = true)
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Bad Request"),
    new ApiResponse(code = 201, message = "Entity Created"),
    new ApiResponse(code = 500, message = "Internal Server Error")
  ))
  def financeAPIPostRoute =
    post {
      entity(as[PortFolio]) {
        portFolio =>
          FinanceRestService.extractCorrelationId { cid =>
            complete {
              val askFutureResponseFinance = breaker.withCircuitBreaker(financeActor ? buildPortfolioKey(cid, portFolio))
              askFutureResponseFinance.collect({
                case enrichedPortfolio: EnrichedPortfolio => {
                  enrichedPortfolio
                }
              }).recover({
                case financeInternalException: FinanceInternalException => {
                  throw financeInternalException
                }
              }
              )
            }
          }
      }
    }

  def buildPortfolioKey(cid: String, portFolio: PortFolio): PortFolioKey = {
    PortFolioKey(cid, portFolio)
  }

}

object FinanceRestService extends Directives {
  def extractCorrelationId = headerValueByName("cid").tflatMap[Tuple1[String]] {
    case Tuple1(cid) =>
      provide(cid)
    case _ =>
      reject(ValidationRejection("cid is not provided"))
  }
}
