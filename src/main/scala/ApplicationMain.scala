import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.util.Timeout
import exoRoutes.RoutesHttp
import scala.concurrent.duration._



object ApplicationMain extends App {
  val routes = new RoutesHttp
  implicit val system = ActorSystem("exoLunatech")
  implicit val materializer = ActorMaterializer()


  implicit val executionContext = system.dispatcher
  implicit val timeout = Timeout(10 seconds)

  Http().bindAndHandle(routes.routes, "0.0.0.0", 8080)
}
