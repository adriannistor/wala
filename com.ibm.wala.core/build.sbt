// set the name of the project
name := "walaCore"

version := "1.0"

organization := "IBM"

sourceDirectory in Compile <<= baseDirectory(_ / "src")

sourceDirectory in Test <<= baseDirectory(_ / "test")

resourceDirectory in Compile <<= baseDirectory(_ / "resources")

EclipseKeys.projectFlavor := EclipseProjectFlavor.Java

autoScalaLibrary := false

crossPaths := false