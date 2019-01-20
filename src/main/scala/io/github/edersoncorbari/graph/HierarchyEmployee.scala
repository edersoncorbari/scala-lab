package io.github.edersoncorbari.graph

import org.apache.spark.sql._
import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD
import scala.util.hashing.MurmurHash3


/**
 * Modeling an Organizational Hierarchy of Employees, using
 * Graph (Top-Down), usually used in large behaviors such as a Bank.
 *
 * This implementation is prepared for 9 hierarchical levels.
 */
case class HierarchyEmployee(sparkSession: SparkSession) extends Serializable {

  private[this] def deepTopLevelHierarcy(vertexNodeDF: DataFrame, edgeRelationShipDF: DataFrame):
    RDD[(Any, (Int, Any, String, Int, Int))] = {
    // Creating a Vertex with primary key, root, path.
    val vRDD = vertexNodeDF
      .rdd
      .map{x => (x.get(0), x.get(1), x.get(2))}
      .map{x => (MurmurHash3.stringHash(x._1.toString).toLong, (x._1.asInstanceOf[Any],
        x._2.asInstanceOf[Any], x._3.asInstanceOf[String]))}

    // Creating an Edge Top-Down of relationships.
    val eRDD = edgeRelationShipDF.rdd.map{x => (x.get(0), x.get(1))}
      .map{x => Edge(MurmurHash3.stringHash(x._1.toString).toLong,
      MurmurHash3.stringHash(x._2.toString).toLong, "topdown")}

    // Creating the graph.
    val graph = Graph(vRDD, eRDD).persist()

    // Get vertices with: (id, level, root, path, iscyclic), existing value of current vertex
    // to build (path, isleaf, emplid).
    val initialGraph = graph.mapVertices((id, v) => (id, 0, v._2, List(v._3), 0, v._3, 1, v._1))
    initialGraph.pregel((0L, 0, 0.asInstanceOf[Any], List(""), 0, 1),
      Int.MaxValue, EdgeDirection.Out)(setMsg, sendMsg, receiveMsg)
        .vertices.map{case(id, v) => (v._8, (v._2, v._3, "/" + v._4.reverse.mkString("/"), v._5, v._7))}
  }

  private[this] def setMsg(vertexId: VertexId, v: (Long, Int, Any, List[String], Int, String, Int, Any), m:
    (Long, Int, Any, List[String], Int, Int)): (Long, Int, Any, List[String], Int, String, Int, Any) = {
    if (m._2 < 1) {
      (v._1, v._2 + 1, v._3, v._4, v._5, v._6, v._7, v._8)
    } else if (m._5 == 1) {
      (v._1, v._2, v._3, v._4, m._5, v._6, v._7, v._8)
    } else if (m._6 == 0) {
      (v._1, v._2, v._3, v._4, v._5, v._6, m._6, v._8)
    } else {
      (m._1, v._2 + 1, m._3, v._6 :: m._4, v._5, v._6, v._7, v._8)
    }
  }

  private[this] def sendMsg(triplet: EdgeTriplet[(Long, Int, Any, List[String], Int, String, Int, Any), _]):
    Iterator[(VertexId, (Long, Int, Any, List[String], Int, Int))] = {
    val (sV, dV) = (triplet.srcAttr, triplet.dstAttr)

    if (sV._1 == triplet.dstId || sV._1 == dV._1)
      if (dV._5 == 0) {
        Iterator((triplet.dstId, (sV._1, sV._2, sV._3, sV._4, 1, sV._7)))
      } else {
        Iterator.empty
      }
    else {
      if (sV._7 == 1) {
        Iterator((triplet.srcId, (sV._1, sV._2, sV._3, sV._4 , 0, 0)))
      }
      else {
        Iterator((triplet.dstId, (sV._1, sV._2, sV._3, sV._4, 0, 1)))
      }
    }
  }

  private[this] def receiveMsg(m1: (Long, Int, Any, List[String], Int, Int),
    m2: (Long, Int, Any, List[String], Int, Int)): (Long, Int, Any, List[String], Int, Int) = {
    m2
  }

  def compute(empStructDF: DataFrame): DataFrame = {
    import sparkSession.implicits._
    val (empVertexNodeDF, empEdgeRelationShipDF) = (
      empStructDF.selectExpr("id", "concat('', '', name)", "concat(name, '', '')"),
      empStructDF.selectExpr("id_connect_by", "id").filter($"id_connect_by".isNotNull))

    val empHirearchyDeepNodeDF = deepTopLevelHierarcy(empVertexNodeDF, empEdgeRelationShipDF)
      .map{case(pk, (level, root, path, iscyclic, isleaf)) => (pk.asInstanceOf[String],
        level, root.asInstanceOf[String], path, iscyclic, isleaf)}
      .toDF("id_connect_by2", "level", "root", "path", "iscyclic", "isleaf").persist()

    empHirearchyDeepNodeDF.join(empStructDF,
      empStructDF.col("id") === empHirearchyDeepNodeDF.col("id_connect_by2"))
      .selectExpr("id", "name", "role", "id_connect_by", "level", "root", "path", "iscyclic", "isleaf")
  }
}
