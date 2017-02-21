import sbt.Keys.version

lazy val pingpongactors = (project in file("."))
    .settings(
      name := "ping-pong-actors",
      version := "1.0",
      scalaVersion := "2.12.1",
      libraryDependencies ++= dependencies
    )

lazy val dependencies = {

  val akkaV = "2.4.17"

  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-testkit" % akkaV,

    "org.scalatest" %% "scalatest" % "3.0.1" % "test",
    "org.mockito" % "mockito-core" % "2.7.5" % "test"
  )
}
