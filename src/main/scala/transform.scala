import org.apache.spark.SparkFiles
import org.apache.spark.sql.types.{LongType, StringType, StructField, StructType}

object transform extends App {
  val session = sparkCxn.spark
  val sc = sparkCxn.spark.sparkContext

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

  val renamed = testDF.withColumnRenamed("_c0","")

  renamed.write.saveAsTable("total_supply")

  val selectTS = session.sql("SELECT * FROM total_supply;")

  selectTS.show()

}
