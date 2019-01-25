package io.github.edersoncorbari.hdfs

import io.github.edersoncorbari.connect.SparkSessionWrapper
import org.scalatest.Matchers


class FsImageTest extends org.scalatest.FunSuite with Matchers with SparkSessionWrapper {

  test("FsImage XML Test") {
    import sparkSession.implicits._
    val fsimageDF = FsImage(sparkSession)
      .compute("test/FSIMAGE/fsimage.xml.gz")

    fsimageDF.show(false)

    assert(fsimageDF.select("blocks").count === 1258)
    assert(fsimageDF.select($"blocks".isNotNull).count === 1258)

    assert(fsimageDF.select("replication").count === 1258)
    assert(fsimageDF.select($"replication".isNotNull).count === 1258)

    stop()
  }

}
