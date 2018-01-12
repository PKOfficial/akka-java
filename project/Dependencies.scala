import java.io

import sbt._

object Dependencies {

  val akkaVersion = "2.5.8"
  val jUnitVersion = "4.12"

  val akka = "com.typesafe.akka" %% "akka-actor" % akkaVersion
  val akkaTestKit = "com.typesafe.akka" %% "akka-testkit" % akkaVersion
  val jUnit = "junit" % "junit" % jUnitVersion

  val helloAkkaDeps: Seq[ModuleID] = Seq(akka)

  val supervisionDeps: Seq[ModuleID] = Seq(
    akka,
    akkaTestKit,
    jUnit
  )

  val routingDeps: Seq[ModuleID] = Seq(akka)

  val hotswapDeps: Seq[ModuleID] = Seq(akka)

}
