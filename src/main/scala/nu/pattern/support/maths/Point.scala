package nu.pattern.support.maths

import scala.collection.mutable
import scala.collection.JavaConversions._

import de.lmu.ifi.dbs.elki.algorithm.clustering.DBSCAN
import de.lmu.ifi.dbs.elki.data.DoubleVector
import de.lmu.ifi.dbs.elki.data.`type`.TypeUtil
import de.lmu.ifi.dbs.elki.database.StaticArrayDatabase
import de.lmu.ifi.dbs.elki.database.ids.DBIDUtil
import de.lmu.ifi.dbs.elki.database.relation.Relation
import de.lmu.ifi.dbs.elki.datasource.ArrayAdapterDatabaseConnection
import de.lmu.ifi.dbs.elki.distance.distancevalue.DoubleDistance
import de.lmu.ifi.dbs.elki.logging.LoggingConfiguration
import de.lmu.ifi.dbs.elki.result.ResultUtil
import de.lmu.ifi.dbs.elki.utilities.ClassGenericsUtil
import de.lmu.ifi.dbs.elki.utilities.optionhandling.parameterization.ListParameterization

case class Point[T](x: T, y: T)

object Point {

  /**
   * Applies the <a href="http://en.wikipedia.org/wiki/DBSCAN">DBSCAN</a> clustering algorithm to the given points.
   *
   * @param points Points to cluster (_D_).
   * @param ε Threshold distance.
   * @param size Minimum cluster size (_MinPts_).
   *
   * @param ev Operations on numeric values.
   *
   * @tparam T Point measurement.
   */
  def clusters[T](points: Seq[Point[T]], ε: T, size: Int)(implicit ev: Numeric[T]): Set[Set[Point[T]]] = {
    LoggingConfiguration.setStatistics()

    val matrix = points.map(p => Array(ev.toDouble(p.x), ev.toDouble(p.y))).toArray[Array[Double]]
    val conn = new ArrayAdapterDatabaseConnection(matrix)
    val db = new StaticArrayDatabase(conn, null)
    db.initialize()

    val relations: Relation[DoubleVector] = db.getRelation(TypeUtil.DOUBLE_VECTOR_FIELD)

    val params = new ListParameterization
    params.addParameter(DBSCAN.Parameterizer.EPSILON_ID, ε)
    params.addParameter(DBSCAN.Parameterizer.MINPTS_ID, size)
    val clustering: DBSCAN[DoubleDistance, _] = ClassGenericsUtil.parameterizeOrAbort(classOf[DBSCAN[_, _]], params)

    val results = ResultUtil.getClusteringResults(clustering.run(db))

    /* Accumulator for clusters. */
    val clusters = mutable.ArrayBuffer.empty[Seq[Point[T]]]

    for (r <- results) {
      for (c <- r.getAllClusters) {
        /* Accumulator for clustered points. */
        val cluster = mutable.ArrayBuffer.empty[Point[T]]

        val i = c.getIDs.iter()
        while (i.valid()) {
          val datum = relations.get(i)
          val index = DBIDUtil.asInteger(i) - 1

          val point = points(index)

          /* Guard against possible API changes where the database index doesn't match with the input sequence index. */
          assert(datum.doubleValue(0) == ev.toDouble(point.x) && datum.doubleValue(1) == ev.toDouble(point.y), "Related datum for index $index does not match the expected point from the source values (expected $datum, got $point).")

          cluster += point
          i.advance()
        }

        clusters += cluster.toSeq
      }
    }

    clusters.map(_.toSet).toSet.filter(_.nonEmpty)
  }


}
