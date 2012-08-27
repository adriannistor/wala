// set the name of the project
name := "walaCore"

version := "1.0"

organization := "IBM"

javaSource in Compile <<= baseDirectory(_ / "src")

scalaSource in Compile <<= baseDirectory(_ / "src")

javaSource in Test <<= baseDirectory(_ / "test")

scalaSource in Test <<= baseDirectory(_ / "test")

resourceDirectory in Compile <<= baseDirectory(_ / "resources")

EclipseKeys.projectFlavor := EclipseProjectFlavor.Java

autoScalaLibrary := false
