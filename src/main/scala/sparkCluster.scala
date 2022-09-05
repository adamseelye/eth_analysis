import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object sparkCluster {
  val spark: SparkSession = SparkSession.builder()
    .appName("Eth Analysis")
    .master("spark://192.168.1.107:7077")
    .config("spark.driver.allowMultipleContexts", "true")

    // need to configure blockmanager
    //.config("spark.sql.warehouse.dir", "hdfs://192.168.1.206:9000/user/hive/warehouse")
    .enableHiveSupport()
    .getOrCreate()

  Logger.getLogger("org").setLevel(Level.ERROR)

  println("~~ Created Spark Session ~~")

}
