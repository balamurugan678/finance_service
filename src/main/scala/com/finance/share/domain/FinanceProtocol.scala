package com.finance.share.domain

import com.finance.share.marshaller.JsonSupport

/**
  * Created by Bala.
  */
object FinanceProtocol extends JsonSupport {

  case class PortFolio(name: String, amount: Double)

  case class PortFolioKey(cid: String, portFolio: PortFolio)

  case class EnrichedPortfolio(correaltionId: String, portFolio: PortFolio, riskAmount: Double)


}

