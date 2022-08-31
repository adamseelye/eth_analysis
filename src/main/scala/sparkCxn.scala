import org.apache.spark.sql.SparkSession
import org.apache.log4j.{Level, Logger}

object sparkCxn {
  val spark: SparkSession = SparkSession.builder()
    .appName("Eth Analysis")
    .master("local[*]")
    .config("spark.driver.allowMultipleContexts", "true")
    .config("spark.sql.warehouse.dir", "hdfs://localhost:9000/user/hive/warehouse")
    .enableHiveSupport()
    .getOrCreate()

  Logger.getLogger("org").setLevel(Level.ERROR)

  println("~~ Created Spark Session ~~")

}
