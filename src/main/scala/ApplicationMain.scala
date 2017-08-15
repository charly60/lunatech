import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.event.LoggingAdapter
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes.InternalServerError
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.Http
import routes.RoutesHttp

class ApplicationMain extends App {
  val routes = new RoutesHttp
  Http().bindAndHandle(routes.routes, "0.0.0.0", 8080)
}
