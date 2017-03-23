package com.finance.share.service

import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.MemberUp
import akka.event.Logging
import com.finance.share.domain.FinanceProtocol.{EnrichedPortfolio, PortFolio, PortFolioKey}
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


class FinanceActor(client: TcpClient) extends Actor with ActorLogging {

  val logger = Logging.getLogger(this)
  val cluster = Cluster(context.system)


  override def preStart() = {
    cluster.subscribe(self, classOf[MemberUp])
    logger.info("The Finance Actor is ready to receive the requests")
  }

  override def postStop() = {
    cluster.unsubscribe(self)
    logger.info("The Finance Actor is gonna stop and would not entertain any requests")
  }

  def receive: Receive = {

    case portFolioKey: PortFolioKey =>
      val enrichedPortfolio = EnrichedPortfolio(portFolioKey.cid, portFolioKey.portFolio, 0.005 * portFolioKey.portFolio.amount)
      client.execute {
        indexInto("finance" / "portfolio") doc (enrichedPortfolio) refresh (RefreshPolicy.IMMEDIATE)
      }
      sender() ! enrichedPortfolio
  }

}
