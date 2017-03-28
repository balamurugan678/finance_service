package com.finance.share.handler

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.{Directives, ExceptionHandler}
import akka.pattern.CircuitBreakerOpenException
import spray.json.DefaultJsonProtocol

/**
 * Global error handler for Finance API
 *
 * Created by Bala.
 */
trait FinanceExceptionHandler extends Directives with SprayJsonSupport with DefaultJsonProtocol {
  import com.finance.share.domain.FinanceProtocol._

  implicit def myExceptionHandler: ExceptionHandler =
    ExceptionHandler {
      case circuitBreakerException: CircuitBreakerOpenException =>
        extractLog { logger =>
            extractUri { uri =>
              val text = s"Request to ${uri} could not be handled normally - Circuit breaker has been activated - ${circuitBreakerException.getMessage}"
              val financeInternalException:FinanceInternalException = FinanceInternalException("FINANCE_902","Upstream server has been down and is not reachable at the moment","ElasticSearch is unreachable and the circuit is open for resiliency")
              complete(ServiceUnavailable, financeInternalException)
            }

        }
      case financeInternalException: FinanceInternalException =>
        extractLog { logger =>
          extractUri { uri =>
            val text = s"Request to ${uri} could not be handled normally "
            complete(ServiceUnavailable, financeInternalException)
          }

        }
    }

}
