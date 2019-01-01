name := "scala-lab"

scalaVersion := "2.11.12"
val sparkVersion = "2.2.0"

libraryDependencies += "org.apache.spark" %% "spark-core" % sparkVersion
libraryDependencies += "org.apache.spark" %% "spark-sql" % sparkVersion
libraryDependencies += "org.apache.spark" %% "spark-graphx" % sparkVersion
libraryDependencies += "org.apache.spark" %% "spark-hive" % sparkVersion

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.2" % Test

resolvers += "Spark Packages Repo" at "http://dl.bintray.com/spark-packages/maven"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", _*) => MergeStrategy.discard
  case _ => MergeStrategy.first
}
test in assembly := {}
assemblyJarName in assembly := "scala-lab.jar"
