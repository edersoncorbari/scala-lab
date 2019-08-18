name := "scala-lab"
organization := "io.github.edersoncorbari"
version := "0.1"
scalaVersion := "2.11.8"
val sparkVersion = "2.1.0"
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

scalastyleConfig := baseDirectory.value / "project/scalastyle-config.xml"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")
scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")

libraryDependencies += "org.apache.spark" %% "spark-core" % sparkVersion
libraryDependencies += "org.apache.spark" %% "spark-sql" % sparkVersion
libraryDependencies += "org.apache.spark" %% "spark-graphx" % sparkVersion
libraryDependencies += "org.apache.spark" %% "spark-hive" % sparkVersion
libraryDependencies += "com.typesafe" % "config" % "1.3.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.0-RC1" % Test
libraryDependencies += "com.databricks" %% "spark-xml" % "0.5.0"

resolvers += "Spark Packages Repo" at "http://dl.bintray.com/spark-packages/maven"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", _*) => MergeStrategy.discard
  case _ => MergeStrategy.first
}
test in assembly := {}
assemblyJarName in assembly := "scala-lab.jar"
