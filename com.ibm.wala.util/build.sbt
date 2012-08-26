// set the name of the project
name := "util"

version := "1.0"

organization := "IBM"

javaSource in Compile <<= baseDirectory(_ / "src")
