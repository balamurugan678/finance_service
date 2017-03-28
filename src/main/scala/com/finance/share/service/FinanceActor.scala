package com.finance.share.service

import akka.actor.{Actor, ActorLogging, Props, Status}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.MemberUp
import akka.event.Logging
import com.finance.share.domain.FinanceProtocol.{EnrichedPortfolio, FinanceInternalException, PortFolioKey}
import com.sksamuel.elastic4s.ElasticDsl.{indexInto, _}
import com.sksamuel.elastic4s.TcpClient
import com.sksamuel.elastic4s.index.RichIndexResponse
import com.sksamuel.elastic4s.jackson.ElasticJackson.Implicits._
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

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

    case portFolioKey: PortFolioKey => {
      val senderRef = sender()
      val enrichedPortfolio = EnrichedPortfolio(portFolioKey.cid, portFolioKey.portFolio, 0.005 * portFolioKey.portFolio.amount)
      val res: Future[RichIndexResponse] = client.execute {
        indexInto("finance" / "portfolio") doc (enrichedPortfolio) refresh (RefreshPolicy.IMMEDIATE)
      }
      res onComplete {
        case Success(s) => senderRef ! enrichedPortfolio
        case Failure(t) => {
          val fin = FinanceInternalException("FINANCE_901","ES Couldn't get connected!!","ElasticSearch server is not responding")
          senderRef ! Status.Failure(fin)
        }

      }
    }


  }

}
