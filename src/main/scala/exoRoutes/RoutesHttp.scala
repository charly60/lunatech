package exoRoutes

import java.sql.ResultSet

import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import core.dataBase._

import scala.util.Try

class RoutesHttp extends AirportQueries with CountryQueries with RunwaysQueries {

  val routes: Route =
    pathPrefix("find") {
      get {
        parameter('nameOrCode) { nameOrCode: String =>
          val response = findByNameOrCode(nameOrCode) match {
            case Some(country) =>
              //TODO utiliser la jointure, plus efficace
              s"""{"country" : "${country.name}", "airports" : ${airportsJsonify(findAirportsByCountry(country.code))} }"""
            case None => "{}"
          }
          complete {
            ToResponseMarshallable(response)
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

  def listToJson(list : List[String]) : String = {
    list.foldLeft("[ ")((acc,str) => s"""$acc "$str" ,""" ).dropRight(1) + " ]"
  }

  def findAirportsAndRunwaysByCountry(countryCode: String) : String = {
    val qqch = selectRunwaysAndAirport(countryCode).groupBy( runwayAndAirport => (runwayAndAirport.airportName, runwayAndAirport.airportType))
    qqch.toList.foldLeft("[ ")( (acc, mapElem) => {
      acc + s"""{ "airportName" : "${mapElem._1._1}", "airportType" : "${mapElem._1._2}" ,"runways" : ${
        listToJson(mapElem._2.map(runwayAndAiport => s"""{ "lighted" : ${runwayAndAiport.lighted}, "closed" : ${runwayAndAiport.closed} }"""))
      } },"""
    } ).dropRight(1)+" ]"
  }

  def SELECT_RUNWAYS_AND_AIRPORTS = PostgresConnection.getConnection
    .map(_.prepareStatement(s"""select type, name, lighted, closed from airport inner join runway on runway.airport_ref = airport.id where airport.iso_country = ?;"""))


  def selectRunwaysAndAirport(countryCode :String) : List[RunwayAndAirport]= {
    SELECT_ALL_RUNWAY match {
      case Some(statement) =>
        val resultSet = statement.executeQuery()
        var listToReturn = List.empty[RunwayAndAirport]
        while (resultSet.next())
          rowToRunwayAndAirport(resultSet) match {
            case Some(runwayAndAirport) => listToReturn ::= runwayAndAirport
            case None => ()
          }
        listToReturn
      case None =>
        println("no connection to postgres")
        List.empty[RunwayAndAirport]
    }
  }

  def rowToRunwayAndAirport(resultSet: ResultSet): Option[RunwayAndAirport] = {
    // Ne fonctionne pas pour une raison inconnue...
    for {
      airportType <- Try(resultSet.getString("type")).toOption
      _ <- Some(println("1: " +airportType))
      name <- Try(resultSet.getString("name")).toOption
      _ <- Some(println("2 : " +name))
      lighted <- Try(resultSet.getInt("lighted")).toOption
      _ <- Some(println("4 :"+lighted))
      closed <- Try(resultSet.getInt("closed")).toOption
      _ <- Some(println("5: " +closed))

    } yield {
      println("OK")
      RunwayAndAirport(
        airportType,
        name,
        lighted,
        closed)
    }
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
      val tupleElem = keyValue._1 -> listOfTypes   // TODO override le toString de List pour faire un vrai Json
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
      acc + s""""${mapElem._1.toString}" : ${listToJson(mapElem._2)},"""
    }).dropRight(1) + " }"
  }

  def tupleToJson(tuple: (List[(String, Int)], List[(String, Int)])): String = {
    def listOfKeyValueToJson(list: List[(String, Int)]) = {
      list.foldLeft("{ ")((acc, keyValue) => {
        acc + s""" "${keyValue._1}" : "${keyValue._2}","""
      }).dropRight(1) + " }"
    }

    val lowNumberOfAirportCountries = listOfKeyValueToJson(tuple._1)
    val hightNumberOfAirportCountries = listOfKeyValueToJson(tuple._2)

    s"""{ "lowNumberOfAirportCountries" : $lowNumberOfAirportCountries , "hightNumberOfAirportCountries" : $hightNumberOfAirportCountries }"""
  }

}
case class RunwayAndAirport(airportType : String, airportName : String, lighted : Int, closed: Int)
