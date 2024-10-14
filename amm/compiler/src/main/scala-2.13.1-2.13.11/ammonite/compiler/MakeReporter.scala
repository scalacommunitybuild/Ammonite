package ammonite.compiler

import ammonite.util.Classpath

import scala.reflect.internal.util.Position
import scala.reflect.io.FileZipArchive
import scala.tools.nsc
import scala.tools.nsc.classpath.{AggregateClassPath, ZipAndJarClassPathFactory}
import scala.tools.nsc.{Global, Settings}
import scala.tools.nsc.interactive.{InteractiveAnalyzer, Global => InteractiveGlobal}
import scala.tools.nsc.plugins.Plugin
import scala.tools.nsc.reporters.FilteringReporter
import scala.tools.nsc.typechecker.Analyzer

object MakeReporter {

  type Reporter = scala.tools.nsc.reporters.Reporter

  def makeReporter(
      errorLogger: (Position, String) => Unit,
      warningLogger: (Position, String) => Unit,
      infoLogger: (Position, String) => Unit,
      outerSettings: Settings
  ): Reporter =
    new FilteringReporter {

<<<<<<< HEAD:amm/compiler/src/main/scala-2.13.1+/ammonite/compiler/MakeReporter.scala
      override def doReport(pos: scala.reflect.internal.util.Position,
                   msg: String,
                   severity: Severity): Unit =
||||||| 151446c5:amm/compiler/src/main/scala-2.13.1+/ammonite/compiler/MakeReporter.scala
      def doReport(pos: scala.reflect.internal.util.Position,
                   msg: String,
                   severity: Severity): Unit =
=======
      def doReport(
          pos: scala.reflect.internal.util.Position,
          msg: String,
          severity: Severity
      ): Unit =
>>>>>>> origin/main:amm/compiler/src/main/scala-2.13.1-2.13.11/ammonite/compiler/MakeReporter.scala
        display(pos, msg, severity)

      def display(pos: Position, msg: String, severity: Severity) =
        severity match {
          case ERROR =>
            Classpath.traceClasspathProblem(s"ERROR: $msg")
            errorLogger(pos, msg)
          case WARNING =>
            warningLogger(pos, msg)
          case INFO =>
            infoLogger(pos, msg)
        }

      def settings = outerSettings
    }
}
