import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkEnv

object diagnosis {
  println(SparkEnv.get.blockManager.blockManagerId)
  Logger.getLogger("org").setLevel(Level.ERROR)

}
