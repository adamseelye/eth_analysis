lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := """eth-analysis-testing""",
    organization := "com.example",
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.12.15",
    libraryDependencies ++= Seq(
      guice,
      "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test
    ),
    scalacOptions ++= Seq(
      "-feature",
      "-deprecation",
      "-Xfatal-warnings"
    )
  )

PlayKeys.devSettings += "play.server.http.port" -> "8080"

// Spark
libraryDependencies += "org.apache.spark" %% "spark-core" % "3.3.0"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.3.0"
libraryDependencies += "org.apache.spark" %% "spark-hive" % "3.3.0"

// circe: JSON
val circeVersion = "0.14.2"

libraryDependencies ++= Seq(
  "io.circe"  %% "circe-core"     % circeVersion,
  "io.circe"  %% "circe-generic"  % circeVersion,
  "io.circe"  %% "circe-parser"   % circeVersion
)