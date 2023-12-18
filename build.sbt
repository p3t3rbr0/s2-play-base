name := """play-base"""
organization := "play-base.org"

version := "0.0.1"

scalaVersion := "2.13.10"

libraryDependencies ++= Seq(
  "com.typesafe.play" % "sbt-plugin" % "2.8.18",
  "org.xerial" % "sqlite-jdbc" % "3.40.0.0",
  "org.playframework.anorm" %% "anorm" % "2.7.0",
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test
)
