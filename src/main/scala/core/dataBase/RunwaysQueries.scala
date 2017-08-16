package core.dataBase

import java.sql.ResultSet

import scala.util.Try

case class Runway(id: Long,
                  airportRef: Long,
                  airportIdent: String,
                  lengthFt: Option[Int],
                  widthFt: Option[Int],
                  surface: Option[String],
                  lighted: Int,
                  closed: Int,
                  leIdent: Option[String],
                  leLatitudeDeg: Option[Double],
                  leLongitudeDeg: Option[Double],
                  leElevationFt: Option[Int],
                  leHeadingDegT: Option[Double],
                  leDisplacedThresholdFt: Option[Int],
                  heIdent: Option[String],
                  heLatitudeDeg: Option[Double],
                  heLongitudeDeg: Option[Double],
                  heElevationFt: Option[Int],
                  heHeadingDegT: Option[String],
                  heDisplacedThresholdFt: Option[Int])

trait RunwaysQueries {

  def SELECT_RUNWAY = PostgresConnection.getConnection
    .map(_.prepareStatement("SELECT id, airport_ref, airport_ident, length_ft, width_ft, surface, lighted, closed, le_ident, le_latitude_deg, le_longitude_deg, le_elevation_ft, le_heading_degT, le_displaced_threshold_ft, he_ident, he_latitude_deg, he_longitude_deg, he_elevation_ft, he_heading_degT, he_displaced_threshold_ft FROM runway WHERE airport_ref = ?;"))

  def SELECT_ALL_RUNWAY = PostgresConnection.getConnection
    .map(_.prepareStatement("SELECT id, airport_ref, airport_ident, length_ft, width_ft, surface, lighted, closed, le_ident, le_latitude_deg, le_longitude_deg, le_elevation_ft, le_heading_degT, le_displaced_threshold_ft, he_ident, he_latitude_deg, he_longitude_deg, he_elevation_ft, he_heading_degT, he_displaced_threshold_ft FROM runway;"))

  def findAllRunways = {
    SELECT_ALL_RUNWAY match {
      case Some(statement) =>
        val resultSet = statement.executeQuery()
        var listToReturn = List.empty[Runway]
        while (resultSet.next())
          rowToRunway(resultSet) match{
            case Some(runway)=> listToReturn ::= runway
            case None => ()
          }
        listToReturn
      case None =>
        println("no connection to postgres")
        List.empty[Runway]
    }
  }





  def findByAirportRef(airportRef: Long): List[Runway] = {
    SELECT_RUNWAY match {
      case Some(statement) =>
        statement.setLong(1, airportRef)
        val resultSet = statement.executeQuery()
        var listToReturn = List.empty[Runway]
        while (resultSet.next())
          rowToRunway(resultSet) match{
            case Some(runway)=> listToReturn ::= runway
            case None => ()
          }
        listToReturn
      case None =>
        println("no connection to postgres")
        List.empty[Runway]
    }
  }


  def rowToRunway(resultSet: ResultSet): Option[Runway] = {
    for {
      id <- Try(resultSet.getLong("id")).toOption
      airportRef <-  Try(resultSet.getLong("airport_ref")).toOption
      airportIdent <- Try(resultSet.getString("airport_indent")).toOption
      lengthFt = Try(resultSet.getInt("length_ft")).toOption
      widthFt = Try(resultSet.getInt("width_ft")).toOption
      surface = Try(resultSet.getString("surface")).toOption
      lighted <- Try(resultSet.getInt("lighted")).toOption
      closed <- Try(resultSet.getInt("closed")).toOption
      leIdent = Try(resultSet.getString("le_ident")).toOption
      leLatitudeDeg = Try(resultSet.getDouble("le_latitude_deg")).toOption
      leLongitudeDeg = Try(resultSet.getDouble("le_longitude_deg")).toOption
      leElevationFt = Try(resultSet.getInt("le_elevation_ft")).toOption
      leHeadingDegT = Try(resultSet.getDouble("le_heading_degT")).toOption
      leDisplacedThresholdFt = Try(resultSet.getInt("le_displaced_threshold_ft")).toOption
      heIdent = Try(resultSet.getString("he_ident")).toOption
      heLatitudeDeg = Try(resultSet.getDouble("he_latitude_deg")).toOption
      heLongitudeDeg = Try(resultSet.getDouble("he_longitude_deg")).toOption
      heElevationFt = Try(resultSet.getInt("he_elevation_ft")).toOption
      heHeadingDegT = Try(resultSet.getString("he_heading_degT")).toOption
      heDisplacedThresholdFt = Try(resultSet.getInt("he_displaced_threshold_ft")).toOption
    } yield {
      Runway(id, airportRef, airportIdent, lengthFt, widthFt, surface, lighted, closed,
        leIdent, leLatitudeDeg, leLongitudeDeg, leElevationFt, leHeadingDegT, leDisplacedThresholdFt,
      heIdent, heLatitudeDeg, heLongitudeDeg, heElevationFt, heHeadingDegT, heDisplacedThresholdFt)
    }
  }

}
