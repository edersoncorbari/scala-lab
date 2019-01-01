package io.github.edersoncorbari

import io.github.edersoncorbari.connect.SparkSessionWrapper
import io.github.edersoncorbari.graph.HierarchyEmployeeService


object HierarchyEmployeeApp extends SparkSessionWrapper {

  def main(args: Array[String]): Unit = {
    import sparkSession.implicits._
    val empDF = sparkSession.sparkContext.parallelize(Array(
       ("1", "Tim Cox", "PRESIDENT", null.asInstanceOf[String]),
       ("2", "Robert Watson", "VP", "1"),
       ("3", "Matthew Powers", "VP", "1"),
       ("4", "Alex Pess", "VP", "1"),
       ("5", "Joao Bastos", "DIRECTOR", "2"),
       ("6", "Martin Jay", "DIRECTOR", "3"),
       ("7", "Anna Becker", "SUPERVISOR", "5"),
       ("8", "Marcos Silverio", "SUPERVISOR", "6"),
       ("9", "Carlos Klaus", "MANAGER", "8"),
       ("10", "Jacob Oliver", "ANALYST", "9"),
       ("11", "Charlie Noah", "ANALYST", "9"),
       ("12", "Claudio Stwart", "ANALYST", "9"),
       ("13", "Jack Connor", "SENIOR DEVELOPER", "10"),
       ("14", "Daniel Mason", "SENIOR DEVELOPER", "10"),
       ("15", "George Reece", "JUNIOR DEVELOPER", "13")))
      .toDF("id", "name", "role", "id_connect_by")

    val graphDF = HierarchyEmployeeService(sparkSession).compute(empDF).sort($"level".asc)
    graphDF.show(false)

    stop()
  }

}
