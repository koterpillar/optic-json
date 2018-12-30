ThisBuild / organization := "org.koterpillar"

val scalaTestVersion = "3.0.5"
val scalaCheckVersion = "1.13.5"
val disciplineVersion = "0.9.0"
val circeVersion = "0.9.1"

lazy val lensJson = project
  .in(file("."))
  .settings(
    name := "lens-json",
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser",
    ).map(_ % circeVersion) ++ Seq(
      "org.scalatest" %% "scalatest" % scalaTestVersion % Test,
    )
  )
