package exoRoutes

import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.stream.scaladsl.{Keep, Sink}
import core.dataBase.{Airport, Runway}
import org.scalatest.{Matchers, WordSpecLike}

import scala.concurrent.Await


class RoutesHttpSpec extends RoutesHttp with WordSpecLike with Matchers with ScalatestRouteTest {

  val airport1 = Airport(id= 1,
    ident= "1",
    airportType= "type",
    name = "airport1",
    latitudeDeg = 0,
    longitudeDeg = 0,
    elevationFt = None,
    continent = "continent",
    isoCountry = "isoCountry1",
    isoRegion = "isoRegion1",
    municipality = None,
    scheduledService = "service1",
    gpsCode = None,
    iataCode = None,
    localCode = None,
    homeLink = None,
    wikipediaLink = None,
    keywords = None)

  val airport2 = Airport(id= 2,
    ident= "2",
    airportType= "type2",
    name = "airport2",
    latitudeDeg = 0,
    longitudeDeg = 0,
    elevationFt = None,
    continent = "continent",
    isoCountry = "isoCountry",
    isoRegion = "isoRegion2",
    municipality = None,
    scheduledService = "service2",
    gpsCode = None,
    iataCode = None,
    localCode = None,
    homeLink = None,
    wikipediaLink = None,
    keywords = None)

  val airport3 = Airport(id= 3,
    ident= "3",
    airportType= "type",
    name = "airport3",
    latitudeDeg = 0,
    longitudeDeg = 0,
    elevationFt = None,
    continent = "continent2",
    isoCountry = "isoCountry",
    isoRegion = "isoRegion3",
    municipality = None,
    scheduledService = "service3",
    gpsCode = None,
    iataCode = None,
    localCode = None,
    homeLink = None,
    wikipediaLink = None,
    keywords = None)

  val jsonAirport = airportsJsonify(List(airport1, airport2, airport3))

  runwaysJsonify(List.empty[Runway]) should be ("[ ]")

  jsonAirport should be (s"""[ { "airportType" : "type", "name" : "airport1", "runways" : [ ] },{ "airportType" : "type2", "name" : "airport2", "runways" : [ { "runway_id" : "232775", "lighted" : "0", "closed": "0" } ] },{ "airportType" : "type", "name" : "airport3", "runways" : [ { "runway_id" : "233754", "lighted" : "1", "closed": "0" } ] } ]""")

  getTypeOfRunwayPerCountry().toString().length should be > 50 // TODO tester le json proprement

  val (lowAirportNumberCountry, highAirportNumberCountry) = getNumberOfAirportPerCountry()

  lowAirportNumberCountry should have size 10
  lowAirportNumberCountry.toString should be ("List((TK,0), (GS,0), (PN,0), (JE,1), (GI,1), (NF,1), (MO,1), (ZZ,1), (BL,1), (CW,1))")

  highAirportNumberCountry should have size 10
  highAirportNumberCountry.toString should be ("List((VE,592), (CO,700), (DE,703), (AR,713), (FR,789), (RU,920), (AU,1908), (CA,2454), (BR,3839), (US,21501))")


  import scala.concurrent.duration._
  val postRequest = HttpRequest(
    HttpMethods.POST,
    uri = "/find",
    entity = HttpEntity(MediaTypes.`application/json`, s"""{ "pays" : "France" }"""))

  postRequest ~> routes ~> check {
    status.isSuccess() shouldEqual true
    Await.result(responseEntity.dataBytes.map(_.decodeString("UTF8")).toMat(Sink.fold("")((acc, elem) => acc + elem))(Keep.right).run, 2 seconds).length should be > 3
  }


  val badPostRequest = HttpRequest(
    HttpMethods.POST,
    uri = "/find",
    entity = HttpEntity(MediaTypes.`application/json`, s"""{ "anyJsonKey" : "anyValue" }"""))

  badPostRequest ~> routes ~> check {
    status.isSuccess() shouldEqual false
  }
}
