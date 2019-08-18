package io.github.edersoncorbari.connect

import org.apache.spark.sql.SparkSession
import io.github.edersoncorbari.Config

trait SparkSessionWrapper extends Config {

  lazy val sparkSession: SparkSession = {
    SparkSession
      .builder()
      .master(appConf.master)
      .appName(appConf.name)
      .enableHiveSupport()
      .getOrCreate()
  }

  lazy val stop = sparkSession.close()
}
