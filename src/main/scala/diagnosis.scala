import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkContext, SparkEnv}
import org.apache.spark.sql.SparkSession

object diagnosis {
  val session: SparkSession =  sparkCluster.spark
  val sc: SparkContext = sparkCluster.spark.sparkContext

  println(sc.sparkUser)
  println(SparkEnv.get.blockManager.blockManagerId)
  Logger.getLogger("org").setLevel(Level.ERROR)

}
