import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object sparkCluster {
  System.setProperty("HADOOP_USER_NAME","data")
  val spark: SparkSession = SparkSession.builder()
    .appName("Ethereum Analysis")
    .master("spark://192.168.1.107:7077")
    .config("spark.driver.allowMultipleContexts", "true")
    .config("spark.executor.memory", "16g")
    .config("spark.sql.warehouse.dir", "hdfs://data-srv:9000/user/hive/warehouse")
    .enableHiveSupport()
    .getOrCreate()

  Logger.getLogger("org").setLevel(Level.ERROR)

  println("~~ Created Spark Session ~~")

}
