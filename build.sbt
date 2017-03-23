name := "financial_service"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= {

  val akkaHTTPVersion = "2.4.2"
  val Json4sVersion = "3.4.1"
  val akkaVersion = "2.4.17"

  Seq(
    "com.typesafe.akka" %% "akka-http-experimental" % akkaHTTPVersion,
    "com.typesafe.akka" %% "akka-http-core" % akkaHTTPVersion,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHTTPVersion,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaHTTPVersion,
    "org.scalatest" %% "scalatest" % "3.0.0" % "test",
    "org.json4s" %% "json4s-native" % Json4sVersion,
    "org.json4s" %% "json4s-ext" % Json4sVersion,
    "de.heikoseeberger" %% "akka-http-json4s" % "1.4.2",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
    "ch.qos.logback" %  "logback-classic" % "1.1.7",
    "com.github.swagger-akka-http" %% "swagger-akka-http" % "0.7.2",
    "co.pragmati" %% "swagger-ui-akka-http" % "1.0.0",
    "com.sksamuel.elastic4s" %% "elastic4s-http" % "5.2.11",
    "com.sksamuel.elastic4s" %% "elastic4s-tcp" % "5.2.11",
    "com.sksamuel.elastic4s" %% "elastic4s-core" % "5.2.11",
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-remote" % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster-metrics" % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
    "com.sksamuel.elastic4s" %% "elastic4s-jackson" % "5.2.11"
  )

}

resolvers += Resolver.bintrayRepo("hseeberger", "maven")