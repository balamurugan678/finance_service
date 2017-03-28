package com.finance.share.service

import akka.actor.ActorSystem
import akka.pattern.CircuitBreaker
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

/**
 * Created by Bala.
 */
trait FinanceCircuitBreaker extends LazyLogging {

  def breakerFinance(system: ActorSystem): CircuitBreaker = {

    val config = ConfigFactory.load()
    val maxFailuresConfig = config.getInt("circuit-breaker.maxFailures")
    val callTimeoutConfig = config.getInt("circuit-breaker.callTimeout")
    val resetTimeoutConfig = config.getInt("circuit-breaker.resetTimeout")

    new CircuitBreaker(
      system.scheduler,
      maxFailures = maxFailuresConfig,
      callTimeout = callTimeoutConfig milliseconds,
      resetTimeout = resetTimeoutConfig milliseconds
    ).
      onOpen(logger.error(s"Finance circuit breaker opened! with the maximum failures $maxFailuresConfig within $callTimeoutConfig milliseconds and would stay open for $resetTimeoutConfig milliseconds")).
      onClose(logger.error(s"Finance circuit breaker closed!")).
      onHalfOpen(logger.error(s"Finance circuit breaker half-open and would pass the first call to the upstream server"))
  }

}
