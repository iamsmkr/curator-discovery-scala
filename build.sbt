ThisBuild / name := "curator-discovery-scala"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.8"

libraryDependencies ++= Seq(
  "org.apache.curator" % "curator-x-discovery" % "5.2.1"
)
