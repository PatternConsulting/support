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

scalaVersion := "2.11.2"

crossScalaVersions := Seq("2.10.4", "2.11.2")

resolvers += "Typesafe (releases)" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "com.meetup" %% "archery" % "0.3.0"
  , "com.typesafe.play" %% "play-json" % "2.3.0"
  , "com.typesafe.scala-logging" %% "scala-logging-slf4j" % "[2.1,)"
  , "org.eu.acolyte" %% "jdbc-scala" % "[1.0,)" % "test"
  , "org.specs2" %% "specs2" % "2.4.2" % "test"
  , "org.spire-math" %% "spire" % "[0.7.5,)"
)
