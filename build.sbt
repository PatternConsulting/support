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

resolvers += "Typesafe (releases)" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.2.3"
  , "com.typesafe.scala-logging" %% "scala-logging-slf4j" % "2.1.2"
  , "org.specs2" %% "specs2" % "2.3.12" % "test"
)
