name := "tubaina-scala"

scalaVersion := "2.9.1"

resolvers += "jboss" at "https://repository.jboss.org/nexus/content/groups/public/"

libraryDependencies <+= scalaVersion((v:String) => "org.scalatest" % ("scalatest_" + v) % "1.6.1" % "test")

libraryDependencies += "junit" % "junit" % "4.9" % "test"

libraryDependencies += "com.novocode" % "junit-interface" % "0.8" % "test"

testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-v")

libraryDependencies ++= Seq(
		"de.java2html" % "java2html" % "5.0",
	"log4j" % "log4j" % "1.2.12",
	"commons-io" % "commons-io" % "1.3.1",
	"commons-cli" % "commons-cli" % "1.1",
	"org.freemarker" % "freemarker" % "2.3.10"
	)
