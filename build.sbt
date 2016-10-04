name := "play-scala"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala, Cinnamon)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws
)

// add cinnamon agent to distribution

import com.typesafe.sbt.packager.Keys.bashScriptExtraDefines

mappings in Universal ++= resolvedJavaAgents.value.map { cinnamon =>
  cinnamon.artifact -> "cinnamon/cinnamon-agent.jar"
}

bashScriptExtraDefines += "addJava -javaagent:${app_home}/../cinnamon/cinnamon-agent.jar"

// add cinnamon akka instrumentation and coda hale metrics

libraryDependencies ++= Seq(
  Cinnamon.library.cinnamonAkka,
  Cinnamon.library.cinnamonCHMetrics
)
