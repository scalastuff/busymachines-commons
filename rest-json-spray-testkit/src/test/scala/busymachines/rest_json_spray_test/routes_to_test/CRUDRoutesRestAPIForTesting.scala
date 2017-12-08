package busymachines.rest_json_spray_test.routes_to_test

import busymachines.rest._

import scala.concurrent.Future

/**
  *
  * You have several options of bringing in your json. Ordered from slowest compile time to fastest:
  * ==========================
  * 1:
  * {{{
  *   busymachines.json._
  *   class AuthenticatedRoutesRestAPIForTesting ... with JsonSupport
  * }}}
  *
  * ==========================
  * 2:
  * {{{
  *   import SomeTestDTOJsonCodec._ //this already extends JsonSupport
  * }}}
  * ==========================
  * 3:
  * {{{
  *   class CRUDRoutesRestAPIForTesting ... with SomeTestDTOJsonCodec
  * }}}
  * ==========================
  *
  *
  * @author Lorand Szakacs, lsz@lorandszakacs.com, lorand.szakacs@busymachines.com
  * @since 07 Sep 2017
  *
  */
private[rest_json_spray_test] class CRUDRoutesRestAPIForTesting
    extends JsonRestAPI with Directives with SomeTestDTOJsonCodec {
  import scala.concurrent.ExecutionContext.Implicits.global

  override protected def routeDefinition: Route =
    pathPrefix("crud") {
      pathEndOrSingleSlash {
        post {
          entity(as[SomeTestDTOPost]) { p =>
            val response = SomeTestDTOGet(
              int    = 42,
              string = p.string,
              option = p.option
            )
            complete((StatusCodes.Created, Future.successful(response)))
          }
        } ~ get {
          val response = Seq(
            SomeTestDTOGet(
              int    = 1,
              string = "one",
              option = None
            ),
            SomeTestDTOGet(
              int    = 2,
              string = "two",
              option = None
            )
          )

          complete(Future.successful(response))
        }
      } ~ path(IntNumber) { id =>
        get {
          val response = SomeTestDTOGet(
            int    = id,
            string = "wabbalubbadubdub",
            option = Option(42)
          )
          complete(Future.successful(response))
        } ~ put {
          entity(as[SomeTestDTOPut]) { p =>
            val response = SomeTestDTOGet(
              int    = id,
              string = p.string,
              option = p.option
            )
            complete(Future.successful(response))
          }
        } ~ patch {
          entity(as[SomeTestDTOPatch]) { p =>
            val response = SomeTestDTOGet(
              int    = id,
              string = p.string,
              option = None
            )
            complete(Future.successful(response))
          }
        } ~ delete {
          complete(StatusCodes.NoContent, Future.unit.asEmptyResponse)
        }
      }
    }
}
