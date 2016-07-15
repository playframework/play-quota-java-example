name := """play-quota-java"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  //#quota-dependency
  "com.lightbend.quota" %% "play-quota" % "0.5.0-alpha-3-SNAPSHOT",
  "com.lightbend.quota" %% "play-quota-java" % "0.5.0-alpha-3-SNAPSHOT"
  //#quota-dependency
)
