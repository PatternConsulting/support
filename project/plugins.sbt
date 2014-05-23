import sbt._

logLevel := Level.Warn

resolvers += "Typesafe (releases)" at "http://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.2.3")

addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.3.1")
