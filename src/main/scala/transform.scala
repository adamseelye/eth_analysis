import io.circe.parser.parse
import io.circe.{Decoder, HCursor, Json}
import org.apache.spark.SparkFiles
import org.apache.spark.sql.types.{LongType, StringType, StructField, StructType}

object transform extends App {
  val session = sparkCxn.spark
  val sc = sparkCxn.spark.sparkContext

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

  def splitExtract(): Unit = {
    val apiMap = extract.apiCalls()
    val ts = apiMap("total_supply")
    val ep = apiMap("eth_price")
    val ns = apiMap("node_size")

    class totalSupply {
      val ts_curled: Json = parse(ts).getOrElse(Json.Null)

      val ts_cursor: HCursor = ts_curled.hcursor
      val ts_json: Decoder.Result[String] = {
        ts_cursor.downField("result").as[String]
      }
      val ts_result: String = ts_json.productElement(0).toString
    }

    class ethereumPrice {
      val ep_curled: Json = parse(ep).getOrElse(Json.Null)

      val ep_cursor: HCursor = ep_curled.hcursor
      val ep_json: Decoder.Result[String] = {
        ep_cursor.downField("result").downField("ethbtc").as[String]
      }
      val ep_result: String = ep_json.productElement(0).toString
    }

    class nodeSize{
      val ns_curled: Json = parse(ns).getOrElse(Json.Null)

      val ns_cursor: HCursor = ns_curled.hcursor
      val ns_json: Decoder.Result[String] = {
        ns_cursor.downField("result").downField("ethbtc").as[String]
      }
      val ns_result: String = ns_json.productElement(0).toString
    }

    val ts_class = new totalSupply
    val ep_class = new ethereumPrice
    val ns_class = new nodeSize

    val extArray = Array(ts_class.ts_result, ep_class.ep_result, ns_class.ns_result)

    for (x <- extArray) {
      println(x)
    }

  }

  splitExtract()

}
