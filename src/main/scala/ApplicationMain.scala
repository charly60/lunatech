import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.util.Timeout
import exoRoutes.RoutesHttp

import scala.concurrent.duration._
import scala.util.Properties



object ApplicationMain extends App {
  val routes = new RoutesHttp
  implicit val system = ActorSystem("exoLunatech")
  implicit val materializer = ActorMaterializer()


  implicit val executionContext = system.dispatcher
  implicit val timeout = Timeout(10 seconds)

  Http().bindAndHandle(routes.routes, Properties.envOrElse("API_BIND", "0.0.0.0"), Properties.envOrElse("API_PORT", "8080").toInt)
}
