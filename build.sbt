// present ambition level is just to build ammonite-ops

scalaVersion := "2.13.4"
organization := "com.lihaoyi"
name := "ammonite-ops"

libraryDependencies += "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2"
libraryDependencies += "com.lihaoyi" %% "os-lib" % "0.7.1"

testFrameworks := Seq(new TestFramework("utest.runner.Framework"))
libraryDependencies += "com.lihaoyi" %% "utest" % "0.7.4" % Test
Test / parallelExecution := false  // not sure if necessary?

Compile / unmanagedSourceDirectories += baseDirectory.value / "ops" / "src" / "main" / "scala"
Compile / unmanagedSourceDirectories += baseDirectory.value / "ops" / "src" / "main" / "scala-2.13"
Test / unmanagedSourceDirectories += baseDirectory.value / "ops" / "src" / "test" / "scala"
Test / unmanagedResourceDirectories += baseDirectory.value / "ops" / "src" / "test" / "resources"

Compile / scalacOptions ++= Seq("-feature", "-deprecation")
