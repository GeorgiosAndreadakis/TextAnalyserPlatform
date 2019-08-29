import sbt.Keys.libraryDependencies

resolvers += "Maven Central" at "http://central.maven.org/maven2"
resolvers += "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases"

name := """Text Analyzer Platform"""
organization := "org.tap"
version := "1.0-SNAPSHOT"

lazy val commonSettings = Seq(
  scalaVersion := "2.11.12"
)

lazy val defaultLibs = Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "junit" % "junit" % "4.12" % Test,
  "com.novocode" % "junit-interface" % "0.11" % Test,
  "org.scalactic" %% "scalactic" % "3.0.1" % Test,
  "org.scalatest" %% "scalatest" % "3.0.1" % Test
)

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .aggregate(domain,application,framework)
  .dependsOn(framework,application,domain)
  .settings(
    commonSettings,
    libraryDependencies ++= defaultLibs,
    libraryDependencies += guice,
    libraryDependencies += "org.projectlombok" % "lombok" % "1.16.10" % Test,
    libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
    libraryDependencies += "info.cukes" %% "cucumber-scala" % "1.2.5" % Test,
    //libraryDependencies += "info.cukes" % "cucumber-guice" % "1.2.5" % Test,
    libraryDependencies += "info.cukes" % "cucumber-junit" % "1.2.5" % Test,
    libraryDependencies += "com.softwaremill.sttp" %% "core" % "1.5.11" % Test,

    resourceDirectory in Test := baseDirectory.value / "test-resources"
  )

lazy val framework =
  project
    .settings(
      commonSettings,
      libraryDependencies ++= defaultLibs,
      libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.19.3",
      libraryDependencies += "org.apache.poi" % "poi-ooxml-schemas" % "3.17",
      libraryDependencies += "stax" % "stax-api" % "1.0.1",
      libraryDependencies += "com.github.jai-imageio" % "jai-imageio-core" % "1.3.1",
      libraryDependencies += "com.levigo.jbig2" % "levigo-jbig2-imageio" % "2.0",
      libraryDependencies += "com.github.jai-imageio" % "jai-imageio-jpeg2000" % "1.3.0",
      libraryDependencies += "org.apache.tika" % "tika-parsers" % "1.17" excludeAll ExclusionRule(organization = "org.slf4j"),
      libraryDependencies += "org.elasticsearch.client" % "elasticsearch-rest-high-level-client" % "7.3.1"
    )
    .dependsOn(application)

lazy val application =
  project
    .settings(
      commonSettings,
      libraryDependencies ++= defaultLibs
    )
    .dependsOn(domain)

lazy val domain =
  project
    .settings(
      commonSettings,
      libraryDependencies ++= defaultLibs
    )


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "org.ac.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "org.ac.binders._"
