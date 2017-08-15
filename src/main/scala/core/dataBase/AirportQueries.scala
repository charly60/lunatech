package core.dataBase

import java.sql.ResultSet

import scala.util.Try

case class Airport(id: Long,
                   ident: String,
                   airportType: String,
                   name: String,
                   latitudeDeg: Double,
                   longitudeDeg: Double,
                   elevationFt: Option[Int],
                   continent: String,
                   isoCountry: String,
                   isoRegion: String,
                   municipality: Option[String],
                   scheduledService: String,
                   gpsCode: Option[String],
                   iataCode: Option[String],
                   localCode: Option[String],
                   homeLink: Option[String],
                   wikipediaLink: Option[String],
                   keywords: Option[String])

trait AirportQueries {
  def SELECT_RUNWAY = PostgresConnection.getConnection
    .map(_.prepareStatement("SELECT id, ident, airportType, name, latitudeDeg, longitudeDeg, elevationFt, continent, isoCountry, isoRegion, municipality, scheduledService, gpsCode, iataCode, localCode, homeLink, wikipediaLink, keywords FROM airport WHERE iso_country = ?;"))

  def findByCountry(isoCountry: String): List[Airport] = {
    SELECT_RUNWAY match {
      case Some(statement) =>
        statement.setString(1, isoCountry)
        val resultSet = statement.executeQuery()
        var listToReturn = List.empty[Airport]
        while (resultSet.next())
          rowToAirport(resultSet) match{
            case Some(airport) => listToReturn ::= airport
            case None => ()
          }
        listToReturn
    }
  }


  def rowToAirport(resultSet: ResultSet): Option[Airport] = {
    for {
      id <- Try(resultSet.getLong("id")).toOption
      ident <- Try(resultSet.getString("ident")).toOption
      airportType <- Try(resultSet.getString("airport_type")).toOption
      name <- Try(resultSet.getString("name")).toOption
      latitudeDeg <- Try(resultSet.getDouble("latitude_deg")).toOption
      longitudeDeg <- Try(resultSet.getDouble("longitude_deg")).toOption
      elevationFt = Try(resultSet.getInt("elevation_ft")).toOption
      continent <- Try(resultSet.getString("continent")).toOption
      isoCountry <- Try(resultSet.getString("iso_country")).toOption
      isoRegion <- Try(resultSet.getString("isoRegion")).toOption
      municipality = Try(resultSet.getString("municipality")).toOption
      scheduledService <- Try(resultSet.getString("scheduled_service")).toOption
      gpsCode = Try(resultSet.getString("gps_code")).toOption
      iataCode = Try(resultSet.getString("iata_code")).toOption
      localCode = Try(resultSet.getString("local_code")).toOption
      homeLink = Try(resultSet.getString("home_link")).toOption
      wikipediaLink = Try(resultSet.getString("wikipedia_link")).toOption
      keywords  = Try(resultSet.getString("keywords")).toOption
    } yield {
      Airport(id,
        ident,
        airportType,
        name,
        latitudeDeg,
        longitudeDeg,
        elevationFt,
        continent,
        isoCountry,
        isoRegion,
        municipality,
        scheduledService,
        gpsCode,
        iataCode,
        localCode,
        homeLink,
        wikipediaLink,
        keywords)
    }
  }
}
