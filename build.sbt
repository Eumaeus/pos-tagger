enablePlugins(ScalaJSPlugin, BuildInfoPlugin)

name := "posTagger"

version := "2.0.0"

scalaVersion := "2.12.4"

resolvers += Resolver.jcenterRepo
resolvers += Resolver.bintrayRepo("neelsmith", "maven")
resolvers += sbt.Resolver.bintrayRepo("denigma", "denigma-releases")

libraryDependencies ++= Seq(
  "org.scala-js" %% "scalajs-stubs" % scalaJSVersion % "provided",
  "org.scala-js" %%% "scalajs-dom" % "0.9.5",
  "io.monix" %%% "monix" % "2.3.0",
  "edu.holycross.shot.cite" %%% "xcite" % "3.3.0",
  "edu.holycross.shot" %%% "ohco2" % "10.8.1",
  "edu.holycross.shot" %%% "scm" % "6.1.0",
  "edu.holycross.shot" %%% "greek" % "1.4.0",
  "edu.holycross.shot" %%% "citeobj" % "7.0.1",
  "edu.holycross.shot" %%% "citerelations" % "2.2.0",
  "edu.holycross.shot" %%% "citebinaryimage" % "1.1.2",
  "com.thoughtworks.binding" %%% "dom" % "latest.version"
)



addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

//scalacOptions += "-P:scalajs:suppressExportDeprecations"
//scalacOptions += "-P:scalajs:suppressMissingJSGlobalDeprecations"
scalacOptions += "-unchecked"
scalacOptions += "-deprecation"


import scala.io.Source
import java.io.PrintWriter

buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion)
buildInfoPackage := "posTagger"
