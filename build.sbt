name := """kcis"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.5"

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "mysql" % "mysql-connector-java" % "5.1.34",
  "com.typesafe.play" %% "play-slick" % "0.8.1",
  "jp.t2v" %% "play2-auth"      % "0.13.0",
  "jp.t2v" %% "play2-auth-test" % "0.13.0" % "test",
  "jp.t2v" %% "stackable-controller" % "0.4.1"
)
