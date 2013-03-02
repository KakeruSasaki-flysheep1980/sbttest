import sbt._
import Keys._

object ApplicationBuild extends Build {

  lazy val main = Project("main", base = file("."))
}
