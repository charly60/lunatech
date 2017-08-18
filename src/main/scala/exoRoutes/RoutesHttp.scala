package exoRoutes

import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.StatusCodes.BadRequest
import akka.http.scaladsl.server.Directives.{complete, pathPrefix, post, _}
import akka.http.scaladsl.server.Route
import core.dataBase._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.Json

import scala.util.Try

class RoutesHttp extends AirportQueries with CountryQueries with RunwaysQueries {

  val routes: Route =
    pathPrefix("find") {
      post {
        entity(as[Json]) { nameOrCodeJson =>
          val nameOrCode = nameOrCodeJson.findAllByKey("pays").headOption.flatMap(_.asString)
          nameOrCode match {
            case Some(nameOrCode) =>
              val response = findByNameOrCode(nameOrCode) match {
                case Some(country) =>
                  s"""{"country" : "${country.name}", "airports" : ${airportsJsonify(findByCountry(country.code))} }"""
                case None => "{}"
              }

              complete {
                ToResponseMarshallable(response)
              }
            case None =>
              complete(BadRequest)
          }
        }
      }
    } ~
      pathPrefix("report") {
        get {
          pathPrefix("type_of_runway") {
            complete {
              ToResponseMarshallable(mapToJson(getTypeOfRunwayPerCountry()))
            }
          } ~
            pathPrefix("number_of_airport") {
              complete {
                ToResponseMarshallable(tupleToJson(getNumberOfAirportPerCountry()))
              }
            }

        }
      }


  def airportsJsonify(airports: List[Airport]): String = {
    val runways = findAllRunways()
    airports.foldLeft("[ ")(
      (json, airport) => {
        json +
          s"""{ "airportType" : "${airport.airportType}", "name" : "${airport.name}", "runways" : ${
            runwaysJsonify(runways.filter(runway => runway.airportRef == airport.id))
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
        Try(refToRunwaysList(airport.id)).toOption match {
          case Some(listOption) => (acc2 ++ listOption.map(_.surface).filter(_.isDefined).flatten).distinct
          case None => acc2
        }
      })
      val tupleElem = keyValue._1 -> listOfTypes
      tupleElem :: acc
    }).toMap
  }


  def getNumberOfAirportPerCountry() = {
    val mapCountryToAirports = findAllAirport().groupBy(airport => airport.isoCountry)
    val otherCountries = findAllCountry().map(country => country.code -> List.empty[Airport]).filter(mapElem => !mapCountryToAirports.contains(mapElem._1))

    val mapCountryToNumberAirport = (mapCountryToAirports ++ otherCountries).map(tuple => (tuple._1, tuple._2.length)).toList.sortWith((tuple1, tuple2) => tuple1._2 < tuple2._2)
    (mapCountryToNumberAirport.take(10), mapCountryToNumberAirport.takeRight(10))
  }

  def mapToJson(map: Map[String, List[String]]): String = {
    map.foldLeft("{ ")((acc, mapElem) => {
      acc + s""""${mapElem._1.toString}" : ${mapElem._2.toString()},"""
    }).dropRight(1) + " }"
  }

  def tupleToJson(tuple: (List[(String, Int)], List[(String, Int)])): String = {
    def listOfKeyValueToJson(list: List[(String, Int)]) = {
      list.foldLeft("{ ")((acc, keyValue) => {
        acc + s""" "${keyValue._1}" : "${keyValue._2},"""
      }).dropRight(1) + " }"
    }

    val lowNumberOfAirportCountries = listOfKeyValueToJson(tuple._1)
    val hightNumberOfAirportCountries = listOfKeyValueToJson(tuple._2)

    s"""{ "lowNumberOfAirportCountries" : $lowNumberOfAirportCountries , "hightNumberOfAirportCountries" : $hightNumberOfAirportCountries }"""
  }

}
