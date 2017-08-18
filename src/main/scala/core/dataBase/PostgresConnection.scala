package core.dataBase

import java.sql.{Connection, DriverManager}

import scala.util.{Properties, Try}


object PostgresConnection {
  def getConnection: Option[Connection] = {
    val pgHost = Properties.envOrElse("POSTGRES_HOST", "localhost")
    val pgPort = Properties.envOrElse("POSTGRES_PORT", "5432")
    val pgDBExploitName = Properties.envOrElse("POSTGRES_DB_NAME", "exolunatech")
    val username = Properties.envOrElse("POSTGRES_USER", "postgres")
    val password = Properties.envOrElse("POSTGRES_PASSWORD", "postgres")
    val url = s"jdbc:postgresql://$pgHost:$pgPort/$pgDBExploitName"

    Try(DriverManager.getConnection(url, username, password)).toOption
  }
}
