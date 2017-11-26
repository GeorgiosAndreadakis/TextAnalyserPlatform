import sbt.Keys.{libraryDependencies, name}

resolvers += "Maven Central" at "http://central.maven.org/maven2"
resolvers += "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases"

testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-v")

lazy val commonSettings = Seq(
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.11.11"
)

lazy val defaultLibs = Seq(
  "junit" % "junit" % "4.12" % Test,
  "com.novocode" % "junit-interface" % "0.11" % Test,
  "org.scalactic" %% "scalactic" % "3.0.1" % Test,
  "org.scalatest" %% "scalatest" % "3.0.1" % Test
)

lazy val root = (project in file("."))
  .aggregate(acceptancetests, framework, application, domain)
  .settings(
    organization := "org.textanalyzerplatform",
    name := "Text Analyzer Platform",
    commonSettings
  )

lazy val acceptancetests =
  project
    .settings(
      commonSettings,
      libraryDependencies ++= defaultLibs,
      libraryDependencies += "info.cukes" % "cucumber-scala_2.11" % "1.2.5" % Test,
      libraryDependencies += "info.cukes" % "cucumber-junit" % "1.2.5" % Test
    )
    .dependsOn(framework)

lazy val framework =
  project
    .settings(
      commonSettings,
      libraryDependencies ++= defaultLibs,
      libraryDependencies += "org.apache.poi" % "poi-ooxml-schemas" % "3.17",
      libraryDependencies += "stax" % "stax-api" % "1.0.1",
      libraryDependencies += "org.apache.tika" % "tika-parsers" % "1.16"
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
