package com.finance.share

import javax.ws.rs.Path

import akka.http.scaladsl.server.Directives
import io.swagger.annotations._

/**
  * Created by Bala.
  */
@Path("/shopping")
@Api(value = "/shopping", description = "Operations about booking", produces = "application/json")
class PriceRestService extends Directives{

  val checkoutRoutes = pathPrefix("shopping") {
    shoppingCheckoutPostRoute
  }

  import com.finance.share.PriceProtocol

  /*val config = ConfigFactory.load()
  val priceApple = config.getDouble("price.apple")
  val priceOrange = config.getDouble("price.orange")*/

  @ApiOperation(value = "Checkout for Shopping Cart", nickname = "shoppingCartCheckout", httpMethod = "GET", consumes = "application/json", produces = "application/json")
  /*@ApiImplicitParams(Array(
    new ApiImplicitParam(name = "GetBooking", dataType = "com.shopping.checkout.domain.PriceProtocol$Cart", paramType = "body", required = true)
  ))*/
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Bad Request"),
    new ApiResponse(code = 201, message = "Entity Created"),
    new ApiResponse(code = 500, message = "Internal Server Error")
  ))
  def shoppingCheckoutPostRoute =
    get {
        complete {
          "Hi"
        }
    }

}
