package core.dataBase

import java.sql.{Connection, DriverManager}

import scala.util.{Properties, Try}


object PostgresConnection {

  var postgresConnection: Option[Connection] = None


  def getConnection: Option[Connection] = {
    postgresConnection match {
      case None =>
        val pgHost = Properties.envOrElse("POSTGRES_HOST", "localhost")
        val pgPort = Properties.envOrElse("POSTGRES_PORT", "5432")
        val pgDBExploitName = Properties.envOrElse("POSTGRES_DB_NAME", "exolunatech")
        val username = Properties.envOrElse("POSTGRES_USER", "postgres")
        val password = Properties.envOrElse("POSTGRES_PASSWORD", "postgres")
        val url = s"jdbc:postgresql://$pgHost:$pgPort/$pgDBExploitName"

        postgresConnection = Try(DriverManager.getConnection(url, username, password)).toOption
        postgresConnection

      case Some(Connection) =>
        postgresConnection
    }

  }
}
