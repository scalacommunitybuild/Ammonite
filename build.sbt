ThisBuild / scalaVersion := "2.13.4"
ThisBuild / organization := "com.lihaoyi"
ThisBuild / Compile / scalacOptions ++= Seq("-feature", "-deprecation")
ThisBuild / libraryDependencies += "com.lihaoyi" %% "utest" % "0.7.5" % Test
ThisBuild / testFrameworks := Seq(new TestFramework("utest.runner.Framework"))
ThisBuild / Test / parallelExecution := false

lazy val root = project.in(file("."))
  .aggregate(ops, terminal, util, interpApi, runtime, replApi)
  .settings(
    publish / skip := true,
  )

val ops = (project in file("ops")).settings(
  name := "ammonite-ops",
  libraryDependencies += "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
  libraryDependencies += "com.lihaoyi" %% "os-lib" % "0.7.1",
)

val terminal = (project in file("terminal")).settings(
  name := "ammonite-terminal",
  libraryDependencies += "com.lihaoyi" %% "sourcecode" % "0.2.1",
  libraryDependencies += "com.lihaoyi" %% "fansi" % "0.2.8",
  Test / unmanagedResourceDirectories += baseDirectory.value / "src" / "test" / "resource",
)

val util = (project in file("amm/util")).settings(
  name := "ammonite-terminal",
  libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  libraryDependencies += "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
  libraryDependencies += "com.lihaoyi" %% "os-lib" % "0.7.1",
  libraryDependencies += "com.lihaoyi" %% "fansi" % "0.2.8",
)

val interpApi = (project in file("amm/interp/api")).settings(
  name := "ammonite-interp-api",
  libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value,
  libraryDependencies += "io.get-coursier" % "interface" % "0.0.21",
).dependsOn(util)

val replApi = (project in file("amm/repl/api")).settings(
  name := "ammonite-repl-api",
  libraryDependencies += "com.lihaoyi" %% "pprint" % "0.5.9",
  libraryDependencies += "com.lihaoyi" %% "mainargs" % "0.1.4",
).dependsOn(ops, interpApi)

val runtime = (project in file("amm/runtime")).settings(
  name := "ammonite-runtime",
  libraryDependencies += "com.lihaoyi" %% "upickle" % "1.2.0",
).dependsOn(replApi)

// val interp = (project in file("amm/interp")).settings(
//   name := "ammonite-interp",
//   Compile / unmanagedSourceDirectories += baseDirectory.value / "src" / "main" / "scala-2.13.1+"
// ).dependsOn(interpApi)
