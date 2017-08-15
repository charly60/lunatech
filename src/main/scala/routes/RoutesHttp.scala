package routes

import javax.swing.text.Segment

import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.headers.`Accept-Language`
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import core.dataBase._

class RoutesHttp extends AirportQueries with CountryQueries with RunwaysQueries {

  val routes: Route =
    pathPrefix("find") {
      (post & formField("message")) { nameOrCode =>
        val response = findByNameOrCode(nameOrCode) match {
          case Some(country) =>
            var jsonToReturn = s"""{"country" : "${country.name}", "airports" : ${airportsJsonify(findByCountry(country.code))} }"""
          case None => "{}"
        }

        complete {
          ToResponseMarshallable(response)
        }
      }
    } ~
  pathPrefix("report"){
    get {
      complete{
        ToResponseMarshallable("TODO repor")
      }
    }
  }



  def airportsJsonify(airports : List[Airport]): String = {
    airports.foldLeft("[")(
      (json,airport) => {
          json +
          s"""{ "airportType" : "${airport.airportType}", "name" : "${airport.name}", "runways" : ${
            runwaysJsonify(findByAirportRef(airport.id))
          } },"""
        }).dropRight(1) + " ]"
  }

  def runwaysJsonify(runways: List[Runway]): String = {
    runways.foldLeft("[")((json,runway) =>{
      json + s"""{ "runway_id" : "${runway.id}", "lighted" : "${runway.lighted}", "closed": "${runway.closed}" },"""
    }).dropRight(1) + " ]"
  }
}
