package exoRoutes

import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.server.Directives.{complete, pathPrefix, post, _}
import akka.http.scaladsl.server.Route
import core.dataBase._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

import scala.util.Try

class RoutesHttp extends AirportQueries with CountryQueries with RunwaysQueries {

  val routes: Route =
    pathPrefix("find") {
      (post & formField("message")) { nameOrCode =>
        val response = findByNameOrCode(nameOrCode) match {
          case Some(country) =>
            s"""{"country" : "${country.name}", "airports" : ${airportsJsonify(findByCountry(country.code))} }"""
          case None => "{}"
        }

        complete {
          ToResponseMarshallable(response)
        }
      }
    } ~
      pathPrefix("report") {
        get {
          complete {
            ToResponseMarshallable("TODO repor")
          }
        }
      }


  def airportsJsonify(airports: List[Airport]): String = {
    airports.foldLeft("[ ")(
      (json, airport) => {
        json +
          s"""{ "airportType" : "${airport.airportType}", "name" : "${airport.name}", "runways" : ${
            runwaysJsonify(findByAirportRef(airport.id))
          } },"""
      }).dropRight(1) + " ]"
  }

  def runwaysJsonify(runways: List[Runway]): String = {
    runways.foldLeft("[ ")((json, runway) => {
      json + s"""{ "runway_id" : "${runway.id}", "lighted" : "${runway.lighted}", "closed": "${runway.closed}" },"""
    }).dropRight(1) + " ]"
  }

  def getTypeOfRunwayPerCountry(): Map[String, List[String]] = {
    val refToRunwaysList = findAllRunways().groupBy(_.airportRef)
    val countryToAirportsList = findAllAirport().groupBy(_.isoCountry)
    countryToAirportsList.foldLeft(List.empty[(String, List[String])])((acc, keyValue) => {
      val listOfTypes = keyValue._2.foldLeft(List.empty[String])((acc2, airport) => {
        Try(refToRunwaysList(airport.id)).toOption match{
          case Some(listOption) =>  (acc2 ++ listOption.map(_.surface).filter(_.isDefined).flatten).distinct
          case None => acc2
        }
      })
      val tupleElem = keyValue._1 -> listOfTypes
      tupleElem :: acc
    }).toMap
  }
}
