import sbt._
import Keys._

object WALABuild extends Build {
    lazy val root = Project(id = "wala",
                            base = file(".")) aggregate(util, shrike, core)

    lazy val util = Project(id = "util",
                           base = file("com.ibm.wala.util"))

    lazy val shrike = Project(id = "shrike",
                           base = file("com.ibm.wala.shrike")).dependsOn(util)

	lazy val core = Project(id = "core",
							base = file("com.ibm.wala.core")).dependsOn(util, shrike)
}