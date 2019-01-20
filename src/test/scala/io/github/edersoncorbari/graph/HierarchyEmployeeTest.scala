package io.github.edersoncorbari.graph

import io.github.edersoncorbari.connect.SparkSessionWrapper
import org.scalatest.Matchers


class HierarchyEmployeeTest extends org.scalatest.FunSuite with Matchers with SparkSessionWrapper {

  test("Graph Hierarchy Employee Test") {
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
      ("15", "George Reece", "JUNIOR DEVELOPER", "13"))
    ).toDF("id", "name", "role", "id_connect_by")

    empDF.show(false)

    assert(empDF.count() == 15)

    val graphDF = HierarchyEmployeeService(sparkSession).compute(empDF)

    graphDF.show(false)

    assert(graphDF.count() == 15)

    assert(graphDF.filter($"role".contains("PRESIDENT")).count() == 1) // Level 1
    assert(graphDF.filter($"role".contains("VP")).count() == 3) // Level 2
    assert(graphDF.filter($"role".contains("DIRECTOR")).count() == 2) // Level 3
    assert(graphDF.filter($"role".contains("SUPERVISOR")).count() == 2) // Level 4
    assert(graphDF.filter($"role".contains("MANAGER")).count() == 1) // Level 5
    assert(graphDF.filter($"role".contains("ANALYST")).count() == 3) // Level 6
    assert(graphDF.filter($"role".contains("SENIOR DEVELOPER")).count() == 2) // Level 7
    assert(graphDF.filter($"role".contains("JUNIOR DEVELOPER")).count() == 1) // Level 8

    import org.apache.spark.sql.functions._
    assert(graphDF.agg(sum("iscyclic")).first.get(0) == 0)
    assert(graphDF.agg(sum("isleaf")).first.get(0) == 6)

    graphDF.select($"path").collect().foreach{x => println(x)}

    stop()
  }

}
