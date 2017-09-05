name := "supervision-monitoring"

version := "1.0"

scalaVersion := "2.12.3"

libraryDependencies ++= Seq("com.typesafe.akka" % "akka-actor_2.12" % "2.5.4",
  "com.typesafe.akka" % "akka-testkit_2.12" % "2.5.4" % "test",
  "junit" % "junit" % "4.12" % "test"
)
