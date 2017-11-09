import sbt.Keys.libraryDependencies

organization := "org.textanalyzerplatform"
name := "Text Analyzer Platform"
version := "0.1.0-SNAPSHOT"
scalaVersion := "2.11.11"

resolvers += "Maven Central" at "http://central.maven.org/maven2"
resolvers += "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases"

testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-v")

lazy val commonSettings = Seq(
  organization := "org.textanalyzerplatform",
  name := "Text Analyzer Platform",
  version := "0.1.0-SNAPSHOT"
)

lazy val defaultLibs = Seq(
  "junit" % "junit" % "4.12" % Test,
  "com.novocode" % "junit-interface" % "0.11" % Test,
  "org.scalactic" %% "scalactic" % "3.0.1" % Test,
  "org.scalatest" %% "scalatest" % "3.0.1" % Test
)


def NewProject(name: String): Project = {
  Project(name, file(name))
}

lazy val root = (project in file("."))
  .aggregate(acceptancetests, application, domain)
  .settings(
    commonSettings
  )

lazy val acceptancetests = (
  NewProject("acceptancetests")
    settings(
    libraryDependencies ++= defaultLibs,
    libraryDependencies += "info.cukes" %% "cucumber-scala" % "1.2.5" % Test,
    libraryDependencies += "info.cukes" % "cucumber-junit" % "1.2.5" % Test,
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % Test
  )
  ) dependsOn application

lazy val application = (
  NewProject("application")
    settings (
      libraryDependencies += "org.apache.poi" % "poi-ooxml-schemas" % "3.16",
      libraryDependencies += "stax" % "stax-api" % "1.0.1",
      libraryDependencies += "org.apache.tika" % "tika-parsers" % "1.15"
    )
  ) dependsOn domain

lazy val domain = (
  NewProject("domain")
    settings (
    )
  )
