package com.finance.share.service

import akka.actor.{Actor, ActorLogging, Props}
import akka.event.Logging
import com.finance.share.domain.FinanceProtocol.PortFolio
import com.sksamuel.elastic4s.ElasticDsl.{indexInto, _}
import com.sksamuel.elastic4s.TcpClient
import com.sksamuel.elastic4s.jackson.ElasticJackson.Implicits._
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy

/**
  * Created by Bala.
  */

object FinanceActor {
  def props(): Props = {
    Props(classOf[FinanceActor])
  }
}


class FinanceActor(client: TcpClient) extends Actor with ActorLogging{

  val logger = Logging.getLogger(this)

  override def preStart() = logger.info("The Finance Actor is ready to receive the requests")

  override def postStop() = logger.info("The Finance Actor is gonna stop and would not entertain any requests")

  def receive: Receive = {

    case portFolio:PortFolio =>
      client.execute {
        indexInto("bands" / "artists") doc(portFolio) refresh(RefreshPolicy.IMMEDIATE)
      }
      sender() ! "It is an actor!!"
  }

}
