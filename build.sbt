ThisBuild / scalaVersion := "2.13.4"
ThisBuild / organization := "com.lihaoyi"
ThisBuild / Compile / scalacOptions ++= Seq("-feature", "-deprecation")
ThisBuild / libraryDependencies += "com.lihaoyi" %% "utest" % "0.7.5" % Test
ThisBuild / testFrameworks := Seq(new TestFramework("utest.runner.Framework"))
ThisBuild / Test / parallelExecution := false

lazy val root = project.in(file("."))
  .aggregate(ops, terminal)
  .settings(
    publish / skip := true,
  )

val ops = (project in file("ops")).settings(
  name := "ammonite-ops",
  libraryDependencies += "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
  libraryDependencies += "com.lihaoyi" %% "os-lib" % "0.7.1",
  Compile / unmanagedSourceDirectories += baseDirectory.value / "src" / "main" / "scala",
  Compile / unmanagedSourceDirectories += baseDirectory.value / "src" / "main" / "scala-2.13",
  Test / unmanagedSourceDirectories += baseDirectory.value / "src" / "test" / "scala",
  Test / unmanagedResourceDirectories += baseDirectory.value / "src" / "test" / "resources",
)

val terminal = (project in file("terminal")).settings(
  name := "ammonite-terminal",
  libraryDependencies += "com.lihaoyi" %% "sourcecode" % "0.2.1",
  libraryDependencies += "com.lihaoyi" %% "fansi" % "0.2.8",
  Compile / unmanagedSourceDirectories += baseDirectory.value / "src" / "main" / "scala",
  Test / unmanagedSourceDirectories += baseDirectory.value / "src" / "test" / "scala",
  Test / unmanagedResourceDirectories += baseDirectory.value / "src" / "test" / "resource",
)
