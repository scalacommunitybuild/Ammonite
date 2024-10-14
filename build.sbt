ThisBuild / scalaVersion := "2.13.15"
ThisBuild / organization := "com.lihaoyi"
ThisBuild / Compile / scalacOptions ++= Seq("-feature", "-deprecation")
ThisBuild / libraryDependencies += "com.lihaoyi" %% "utest" % "0.8.4" % Test
ThisBuild / testFrameworks := Seq(new TestFramework("utest.runner.Framework"))
ThisBuild / Test / parallelExecution := false
ThisBuild / evictionErrorLevel := Level.Info

lazy val root = project.in(file("."))
  .aggregate(terminal, util, interpApi, compiler, compilerInterface, runtime, replApi, repl, interp, amm, shell, integration)
  .settings(
    publish / skip := true,
  )

val terminal = (project in file("terminal")).settings(
  name := "ammonite-terminal",
  libraryDependencies += "com.lihaoyi" %% "sourcecode" % "0.4.3-M1",
  libraryDependencies += "com.lihaoyi" %% "fansi" % "0.5.0",
  Test / unmanagedResourceDirectories += baseDirectory.value / "src" / "test" / "resource",
)

val util = (project in file("amm/util")).settings(
  name := "ammonite-util",
  libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  libraryDependencies += "org.scala-lang.modules" %% "scala-collection-compat" % "2.12.0",
  libraryDependencies += "com.lihaoyi" %% "os-lib" % "0.10.7",
  libraryDependencies += "com.lihaoyi" %% "fansi" % "0.5.0",
  libraryDependencies += "org.tpolecat" %% "typename" % "1.1.0",

)

val compilerInterface = (project in file("amm/compiler/interface")).settings(
  name := "ammonite-compiler-interface",
).dependsOn(util)

val interpApi = (project in file("amm/interp/api")).settings(
  name := "ammonite-interp-api",
  libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value,
  libraryDependencies += "io.get-coursier" % "interface" % "1.0.19",
).dependsOn(util, compilerInterface)

val replApi = (project in file("amm/repl/api")).settings(
  name := "ammonite-repl-api",
  libraryDependencies += "com.lihaoyi" %% "pprint" % "0.9.0",
  libraryDependencies += "com.lihaoyi" %% "mainargs" % "0.7.5",
  Compile / unmanagedSourceDirectories += baseDirectory.value / "src" / "main" / "scala-2.13-or-3",
).dependsOn(interpApi)

val compiler = (project in file("amm/compiler")).settings(
  name := "ammonite-compiler",
  libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value,
  libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "2.0.1",
  libraryDependencies += "com.lihaoyi" %% "scalaparse" % "3.1.1",
  libraryDependencies += "com.github.javaparser" % "javaparser-core" % "3.2.12",
  libraryDependencies += "org.javassist" % "javassist" % "3.21.0-GA",
  Compile / unmanagedSourceDirectories += baseDirectory.value / "src" / "main" / "scala-2.13.1+",
  Compile / unmanagedSourceDirectories += baseDirectory.value / "src" / "main" / "scala-2.13.12+",
  Compile / unmanagedSourceDirectories += baseDirectory.value / "src" / "main" / "scala-2.12.10-2.13.1+",
  Test / unmanagedSourceDirectories += baseDirectory.value / "src" / "test" / "scala-2",
).dependsOn(util, replApi, compilerInterface)

val runtime = (project in file("amm/runtime")).settings(
  name := "ammonite-runtime",
  libraryDependencies += "com.lihaoyi" %% "upickle" % "4.0.2",
  libraryDependencies += "io.get-coursier" % "interface" % "1.0.19",
  libraryDependencies += "io.get-coursier" %% "class-path-util" % "0.1.4",
  libraryDependencies += "io.get-coursier" %% "dependency-interface" % "0.2.3",
).dependsOn(replApi, interpApi)

val interp = (project in file("amm/interp")).settings(
  name := "ammonite-interp",
  libraryDependencies += "com.lihaoyi" %% "scalaparse" % "3.1.1",
  libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "2.0.1",
  libraryDependencies += "ch.epfl.scala" % "bsp4j" % "2.1.0-M5",
  libraryDependencies += "org.scalameta" %% "trees" % "4.9.7",
  Compile / unmanagedSourceDirectories += baseDirectory.value / "src" / "main" / "scala-2.13.1+",
  Compile / unmanagedSourceDirectories += baseDirectory.value / "src" / "main" / "scala-2.12.10-2.13.1+",
).dependsOn(runtime, compilerInterface, replApi)

val repl = (project in file("amm/repl")).settings(
  name := "ammonite-repl",
  libraryDependencies += "org.javassist" % "javassist" % "3.21.0-GA",
  libraryDependencies += "com.github.javaparser" % "javaparser-core" % "3.2.12",
).dependsOn(replApi, terminal, interp, compiler)

val amm = (project in file("amm")).settings(
  name := "ammonite",
  libraryDependencies += "org.scala-lang.modules" %% "scala-java8-compat" % "1.0.2" % Test,
  libraryDependencies += "com.lihaoyi" %% "acyclic" % "0.3.15" cross CrossVersion.full,
).dependsOn(compiler % "compile->compile;test->test", repl % "compile->compile;test->test")

val shell = (project in file("shell")).settings(
  name := "ammonite-shell",
  Test / fork := true,
  Test / envVars := Map("AMMONITE_SHELL" -> (Compile / packageBin).value.toString),
).dependsOn(amm % "compile->compile;test->test")

val integration = (project in file("integration")).settings(
  name := "ammonite-integration",
  libraryDependencies += "com.lihaoyi" %% "cask" % "0.9.1",
).dependsOn(amm % "compile->compile;test->test")
