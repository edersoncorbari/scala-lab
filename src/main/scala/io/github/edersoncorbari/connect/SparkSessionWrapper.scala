package io.github.edersoncorbari.connect

import org.apache.spark.sql.SparkSession

trait SparkSessionWrapper {

  lazy val sparkSession: SparkSession = {
    SparkSession
      .builder()
      .master("local")
      .appName("scala-lab")
      .getOrCreate()
  }

  def stop() = sparkSession.close()
}
