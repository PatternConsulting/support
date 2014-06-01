import sbt._

logLevel := Level.Warn

resolvers += "Typesafe (releases)" at "http://repo.typesafe.com/typesafe/releases/"

/* See http://stackoverflow.com/a/20497429 for details. */
resolvers += "Typesafe (releases, Maven)" at "http://repo.typesafe.com/typesafe/simple/maven-releases/"

/* As of June 1, 2014, Typesafe's repositories are missing JNotify. This clone has the dependencies required by sbt-plugin, but this is only a band-aid fix. */
resolvers += "Element (Typesafe clone)" at "http://repo.element.hr/nexus/content/repositories/typesafe-repo/"

/* Preferring Typesafe's sbt-plugin over https://github.com/mpeltonen/sbt-idea as the latter fails to include all dependencies in the IDEA project. */
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.3.0")

addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.3.1")
