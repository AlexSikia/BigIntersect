ThisBuild / scalaVersion := "2.13.1"
ThisBuild / organization := "alexandre.sikiaridis"

lazy val bigintersect = (project in file("."))
  .settings(
    name := "BigIntersect",
    libraryDependencies += "org.scala-lang.modules" %% "scala-parallel-collections" % "0.2.0",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.0" % Test
  )