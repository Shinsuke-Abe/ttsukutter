import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "ttsukutter"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
      "org.twitter4j" % "twitter4j-core" % "[2.2,)",
      "postgresql" % "postgresql" % "9.1-901-1.jdbc4"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      // Add your own project settings here
      resolvers += "twitter4j.org Repository" at "http://twitter4j.org/maven2"
    )

}
