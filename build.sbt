import sbtbuildinfo.Plugin._

organization in ThisBuild := "nu.pattern"

name := "support"

version in ThisBuild := "./version.sh".!!.trim.drop(1)

buildInfoSettings

sourceGenerators in Compile <+= buildInfo

buildInfoKeys := Seq[BuildInfoKey](
  name,
  version,
  scalaVersion,
  sbtVersion,
  "datetime" -> System.currentTimeMillis
)

buildInfoPackage := "nu.pattern.support"

scalaVersion := "2.11.1"

resolvers += "Typesafe (releases)" at "http://repo.typesafe.com/typesafe/releases/"

/* See http://stackoverflow.com/a/20497429 for details. */
resolvers += "Typesafe (releases, Maven)" at "http://repo.typesafe.com/typesafe/simple/maven-releases/"

libraryDependencies ++= Seq(
  "com.meetup" %% "archery" % "0.3.0"
  , "com.typesafe.play" %% "play-json" % "2.3.0"
  , "com.typesafe.scala-logging" %% "scala-logging-slf4j" % "2.1.2"
  , "org.specs2" %% "specs2" % "2.3.12" % "test"
  , "org.spire-math" %% "spire" % "0.7.5"
)
