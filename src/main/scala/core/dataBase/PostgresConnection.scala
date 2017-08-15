package core.dataBase

import java.sql.{Connection, DriverManager}

import scala.util.Try


object PostgresConnection {
  def getConnection: Option[Connection] = {
      val pgHost = "localhost"
      val pgPort = 5432
      val pgDBExploitName = "exolunatech"
      val username = "postgres"
      val password = "postgres"
      val url = s"jdbc:postgresql://$pgHost:$pgPort/$pgDBExploitName"

      Try(DriverManager.getConnection(url, username, password)).toOption
  }
}
