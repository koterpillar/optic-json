ThisBuild / organization := "org.koterpillar"

resolvers += "jitpack" at "https://jitpack.io"

val scalaTestVersion = "3.0.5"
val scalaCheckVersion = "1.13.5"
val disciplineVersion = "0.9.0"
val circeVersion = "0.9.1"
val catsVersion = "1.0.0"
val monocleVersion = "1.5.0"
val shapelessVersion = "2.3.3"
val jsonSchemaVersion = "1.11.0"

lazy val lensJson = project
  .in(file("."))
  .settings(
    name := "lens-json",
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion,

      "com.github.everit-org.json-schema" % "org.everit.json.schema" % jsonSchemaVersion,

      "org.scalatest" %% "scalatest" % scalaTestVersion % Test,

      "org.typelevel" %% "cats-core" % catsVersion,
      "org.typelevel" %% "cats-laws" % catsVersion,
      
      "com.github.julien-truffaut" %%  "monocle-core"  % monocleVersion,
      "com.github.julien-truffaut" %%  "monocle-macro" % monocleVersion,
      "com.github.julien-truffaut" %%  "monocle-law"   % monocleVersion % Test,

      "com.chuusai" %% "shapeless" % shapelessVersion,
    )
  )
