ThisBuild / scalaVersion := "2.13.4"
ThisBuild / organization := "com.lihaoyi"
ThisBuild / Compile / scalacOptions ++= Seq("-feature", "-deprecation")
ThisBuild / libraryDependencies += "com.lihaoyi" %% "utest" % "0.7.5" % Test
ThisBuild / testFrameworks := Seq(new TestFramework("utest.runner.Framework"))
ThisBuild / Test / parallelExecution := false

lazy val root = project.in(file("."))
  .aggregate(ops, terminal, util, interpApi, runtime, replApi, repl, interp, amm, shell)
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
  name := "ammonite-util",
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

val interp = (project in file("amm/interp")).settings(
  name := "ammonite-interp",
  libraryDependencies += "com.lihaoyi" %% "scalaparse" % "2.3.0",
  libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.2.0",
  libraryDependencies += "ch.epfl.scala" % "bsp4j" % "2.0.0-M6",
  libraryDependencies += "org.scalameta" %% "trees" % "4.4.0",
  Compile / unmanagedSourceDirectories += baseDirectory.value / "src" / "main" / "scala-2.13.1+",
  Compile / unmanagedSourceDirectories += baseDirectory.value / "src" / "main" / "scala-2.12.10-2.13.1+",
).dependsOn(runtime)

val repl = (project in file("amm/repl")).settings(
  name := "ammonite-repl",
  libraryDependencies += "org.javassist" % "javassist" % "3.21.0-GA",
  libraryDependencies += "com.github.javaparser" % "javaparser-core" % "3.2.5",
).dependsOn(replApi, terminal, interp)

val amm = (project in file("amm")).settings(
  name := "ammonite",
  libraryDependencies += "org.scala-lang.modules" %% "scala-java8-compat" % "0.9.0" % Test,
).dependsOn(repl % "compile->compile;test->test")

val shell = (project in file("shell")).settings(
  name := "ammonite-shell",
  Test / fork := true,
  Test / envVars := Map("AMMONITE_SHELL" -> (Compile / packageBin).value.toString),
).dependsOn(amm % "compile->compile;test->test")
