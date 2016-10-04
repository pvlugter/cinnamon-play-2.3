# Play 2.3 with Cinnamon

Sample project for adding Cinnamon agent to Play 2.3 distribution.

## Add Cinnamon agent to distribution

Add [credentials](http://developer.lightbend.com/docs/monitoring/latest/setup/cinnamon-agent-sbt.html#bintray-credentials) to `~/.lightbend/commercial.credentials`.

Add sbt-cinnamon plugin in `project/plugins.sbt`:

```scala
addSbtPlugin("com.lightbend.cinnamon" % "sbt-cinnamon" % "2.0.0")

credentials += Credentials(Path.userHome / ".lightbend" / "commercial.credentials")

resolvers += Resolver.url("lightbend-commercial", url("https://repo.lightbend.com/commercial-releases"))(Resolver.ivyStylePatterns)
```

Enable Cinnamon plugin on Play project in `build.sbt`:

```scala
lazy val root = (project in file(".")).enablePlugins(PlayScala, Cinnamon)
```

Add sbt settings to enable Cinnamon agent in the distribution. This is done automatically by the sbt-cinnamon plugin in newer versions of Play, but needs to be added manually for Play 2.3. In `build.sbt`:

```scala
import com.typesafe.sbt.packager.Keys.bashScriptExtraDefines

mappings in Universal ++= resolvedJavaAgents.value.map { cinnamon =>
  cinnamon.artifact -> "cinnamon/cinnamon-agent.jar"
}

bashScriptExtraDefines += "addJava -javaagent:${app_home}/../cinnamon/cinnamon-agent.jar"
```

Then add instrumentation/reporter dependencies and actor configuration. See the [documentation](http://developer.lightbend.com/docs/monitoring/latest/home.html). For example, adding the Coda Hale Metrics console reporter:

In `build.sbt`:

```scala
libraryDependencies ++= Seq(
  Cinnamon.library.cinnamonAkka,
  Cinnamon.library.cinnamonCHMetrics
)
```

In `application.conf`:

```conf
cinnamon {
  chmetrics {
    reporters += console-reporter
  }

  akka.actors {
    "/user/*" {
      report-by = class
    }
  }
}
```

## Run distribution

First `stage` the distribution and then run it:

```
sbt stage
target/universal/stage/bin/play-scala
```

Or use the `sbt start` command.
