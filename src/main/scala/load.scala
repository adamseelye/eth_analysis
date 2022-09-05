import org.apache.spark.{SparkContext, SparkFiles}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{LongType, StringType, StructField, StructType}

object load extends App {
  val session: SparkSession = sparkCluster.spark
  val sc: SparkContext = sparkCluster.spark.sparkContext

  val dataMap: Map[String, String] = transform.dataTransform()
  val testMap: Map[String, String] = Map("one" -> "uno", "two" -> "dos", "three" -> "tres")


  def tsFrame(): Unit = {
    import session.implicits._

    val supply = dataMap("totalsupply")
    println(supply)

    //val df = dataMap("totalsupply").toSeq.toDS()
    //val df = testMap.toSeq.toDF("english", "spanish")

    //df.show()

  }

  tsFrame()

  def epFrame(): Unit = {

  }

  def nsFrame(): Unit = {

  }

  def ncFrame(): Unit = {

  }

  def goFrame(): Unit = {

  }

  def csvLoad(): Unit = {
    val schema = StructType(
      Array(
        StructField("", LongType, nullable = true),
        StructField("status", StringType, nullable = false),
        StructField("message", StringType, nullable = false),
        StructField("result", StringType, nullable = false)
      )
    )

    val urlFile = "data/total_supply.csv"

    sc.addFile(urlFile)

    var df = session
      .read
      .schema(schema)
      .format("csv")
      .option("header", "true")
      .load("file://" + SparkFiles.get("total_supply.csv"))

    df = df.select(
      df(""),
      df("status"),
      df("message"),
      df("result")
    )

    val testDF = session.read.format("csv").option("header", "true").load("hdfs://localhost:9000/testing/total_supply.csv")
    testDF.createOrReplaceTempView("TestView")

    val renamed = testDF.withColumnRenamed("_c0", "")

    renamed.write.saveAsTable("total_supply")

    val selectTS = session.sql("SELECT * FROM total_supply;")

    selectTS.show()

  }

}
