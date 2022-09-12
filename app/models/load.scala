import org.apache.spark.{SparkContext, SparkFiles}
import org.apache.spark.sql.{DataFrame, Row, SQLContext, SaveMode, SparkSession}
import org.apache.spark.sql.types.{LongType, StringType, StructField, StructType}

package models {

object load {
  val session: SparkSession = models.sparkCluster.session
  val sc: SparkContext = models.sparkCluster.session.sparkContext

  import session.implicits._

  val dataMap: Map[String, String] = models.transform.dataTransform()

  def tsFrame(): DataFrame = {
    val supply = Seq(dataMap("totalsupply"))
    val df = supply.toDF("total supply")

    df
  }

  def epFrame(): DataFrame = {
    val eth_price = Seq(Row(dataMap("ethbtc"), dataMap("ethusd"), dataMap("pricetime")))

    val dataStruct = StructType(Seq(
      StructField("ETH/BTC", StringType, nullable = true),
      StructField("ETH/USD", StringType, nullable = true),
      StructField("Timestamp", StringType, nullable = true)
    ))

    val rdd = sc.parallelize(eth_price)
    val df = session.createDataFrame(rdd, dataStruct)

    df
  }

  def nsFrame(): DataFrame = {
    val node_size = Seq(Row(dataMap("blockheight"), dataMap("chaintime"), dataMap("chainsize")))

    val dataStruct = StructType(Seq(
      StructField("Block Height", StringType, nullable = true),
      StructField("Chain Timestamp", StringType, nullable = true),
      StructField("Chain Size (bytes)", StringType, nullable = true)
    ))

    val rdd = sc.parallelize(node_size)
    val df = session.createDataFrame(rdd, dataStruct)

    df
  }

  def ncFrame(): DataFrame = {
    val node_count = Seq(Row(dataMap("utctime"), dataMap("nodecount")))

    val dataStruct = StructType(Seq(
      StructField("Timestamp", StringType, nullable = true),
      StructField("Node Count", StringType, nullable = true)
    ))

    val rdd = sc.parallelize(node_count)
    val df = session.createDataFrame(rdd, dataStruct)

    df
  }

  def goFrame(): DataFrame = {
    val gas_oracle = Seq(Row(dataMap("safeprice"), dataMap("suggested"), dataMap("fastprice"), dataMap("basefee")))

    val dataStruct = StructType(Seq(
      StructField("Secure Gas Price", StringType, nullable = true),
      StructField("Suggested Gas Price", StringType, nullable = true),
      StructField("Fast Gas Price", StringType, nullable = true),
      StructField("Base Transaction Fee", StringType, nullable = true)
    ))

    val rdd = sc.parallelize(gas_oracle)
    val df = session.createDataFrame(rdd, dataStruct)

    df
  }

  def writeCSV(): Unit = {
    val hdfsPath = "hdfs://data-srv:9000/user/hive/warehouse/eth_analysis"

    tsFrame().write.mode(SaveMode.Overwrite).option("header", value = true).csv(s"$hdfsPath/tscsv")
    epFrame().write.mode(SaveMode.Overwrite).option("header", value = true).csv(s"$hdfsPath/epcsv")
    nsFrame().write.mode(SaveMode.Overwrite).option("header", value = true).csv(s"$hdfsPath/nscsv")
    ncFrame().write.mode(SaveMode.Overwrite).option("header", value = true).csv(s"$hdfsPath/nccsv")
    goFrame().write.mode(SaveMode.Overwrite).option("header", value = true).csv(s"$hdfsPath/gocsv")
  }


  def writeJSON(): Unit = {
    val hdfsPath = "hdfs://data-srv:9000/user/hive/warehouse/eth_analysis"

    tsFrame().write.mode("append").json(s"$hdfsPath/tsjson")
    epFrame().write.mode("append").json(s"$hdfsPath/epjson")
    nsFrame().write.mode("append").json(s"$hdfsPath/nsjson")
    ncFrame().write.mode("append").json(s"$hdfsPath/ncjson")
    goFrame().write.mode("append").json(s"$hdfsPath/gojson")
  }
}
}
