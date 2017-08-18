name := "lunatech"

version := "1.0"

scalaVersion := "2.12.3"



libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.0.6",
  "com.typesafe.akka" %% "akka-http-xml" % "10.0.6",
  "org.postgresql" % "postgresql" % "42.0.0",
  "de.heikoseeberger" %% "akka-http-circe" % "1.13.0",

  "com.typesafe.akka" %% "akka-http-testkit" % "10.0.6" % Test,
  "org.scalatest" %% "scalatest" % "3.0.3" % Test,
  "org.mockito" % "mockito-core" % "2.7.22" % Test
)
libraryDependencies ++= Seq(
  "io.circe" %% "circe-core" % "0.7.1",
  "io.circe" %% "circe-generic" % "0.7.1",
  "io.circe" %% "circe-parser" % "0.7.1"
)