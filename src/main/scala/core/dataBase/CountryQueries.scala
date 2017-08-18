package core.dataBase

import java.sql.ResultSet

import scala.util.Try


case class Country(id: Long,
                   code: String,
                   name: String,
                   continent: String,
                   wikipediaLink: String,
                   keywords: Option[String])


trait CountryQueries {

  def SELECT_COUNTRY = PostgresConnection.getConnection
    .map(_.prepareStatement("SELECT id, code, name, continent, wikipedia_link, keywords FROM country WHERE name = ? OR code = ?;"))

  def SELECT_ALL_COUNTRY = PostgresConnection.getConnection
    .map(_.prepareStatement("SELECT id, code, name, continent, wikipedia_link, keywords FROM country;"))


  def findAllCountry(): List[Country] = {
    SELECT_ALL_COUNTRY match {
      case Some(statement) =>
        val resultSet = statement.executeQuery()
        var listToReturn = List.empty[Country]
        while (resultSet.next())
          rowToCountry(resultSet) match {
            case Some(airport) => listToReturn ::= airport
            case None => ()
          }
        listToReturn
      case None =>
        println("no connection to postgres")
        List.empty[Country]
    }
  }

  def findByNameOrCode(nameOrCode: String): Option[Country] = {
    SELECT_COUNTRY match {
      case Some(statement) =>
        statement.setString(1, nameOrCode)
        statement.setString(2, nameOrCode)
        val resultSet = statement.executeQuery()
        if (resultSet.next())
          rowToCountry(resultSet)
        else None
      case None =>
        println("no connection to postgres")
        None
    }
  }


  def rowToCountry(resultSet: ResultSet): Option[Country] = {
    for {
      id <- Try(resultSet.getLong("id")).toOption
      code <- Try(resultSet.getString("code")).toOption
      name <- Try(resultSet.getString("name")).toOption
      continent <- Try(resultSet.getString("continent")).toOption
      wikipediaLink <- Try(resultSet.getString("wikipedia_link")).toOption
      keywords = Try(resultSet.getString("keywords")).toOption
    } yield {
      Country(id, code, name, continent, wikipediaLink, keywords)
    }
  }
}
