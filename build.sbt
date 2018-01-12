import Dependencies._

name := "akka-java"

version := "0.1"

scalaVersion := "2.12.4"

lazy val root = (project in file("."))
  .aggregate(helloAkka, supervision)

lazy val helloAkka = (project in file("hello-akka"))
  .settings(
    libraryDependencies ++= helloAkkaDeps
  )

lazy val supervision = (project in file("supervision"))
  .settings(
    libraryDependencies ++= supervisionDeps
  )

lazy val routing = (project in file("routing"))
  .settings(
    libraryDependencies ++= routingDeps
  )
