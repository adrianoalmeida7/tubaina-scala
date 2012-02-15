name := "tubaina-scala"

scalaVersion := "2.9.1"

libraryDependencies <+= scalaVersion((v:String) => "org.scalatest" % ("scalatest_" + v) % "1.6.1" % "test")