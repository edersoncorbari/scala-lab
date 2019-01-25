package io.github.edersoncorbari

import io.github.edersoncorbari.connect.SparkSessionWrapper
import io.github.edersoncorbari.hdfs.FsImage


object FsImageApp extends SparkSessionWrapper {

  def main(args: Array[String]): Unit = {
    FsImage(sparkSession)
      .compute("test/FSIMAGE/fsimage.xml.gz")
      .show(false)

    stop()
  }

}
