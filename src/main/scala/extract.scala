import org.apache.spark.sql.SaveMode

import scala.language.postfixOps
import sys.process._


object extract extends App {
  val session = sparkCluster.spark
  val sc = sparkCluster.spark.sparkContext

  def apiCalls(): Map[String, String] = {
    val apiToken = ""

    val apiMap: Map[String, String] = Map(
      "total_supply" -> f"https://api.etherscan.io/api?module=stats&action=ethsupply&apikey=${apiToken}",
      "eth_price" -> f"https://api.etherscan.io/api?module=stats&action=ethprice&apikey=${apiToken}",
      "node_size" -> f"https://api.etherscan.io/api?module=stats&action=chainsize&startdate=2019-02-01&enddate=2019-02-28&clienttype=geth&syncmode=default&sort=asc&apikey=${apiToken}"
    )

    val curl_ts: String = s"curl ${apiMap("total_supply")}" !!
    val curl_ep: String = s"curl ${apiMap("eth_price")}" !!
    val curl_ns: String = s"curl ${apiMap("node_size")}" !!

    val curlMap: Map[String, String] = Map(
      "total_supply" -> curl_ts,
      "eth_price" -> curl_ep,
      "node_size" -> curl_ns
    )

    curlMap

  }

  println(apiCalls()("total_supply"))

//  import session.implicits._
//  val df = session.read.json(Seq(curl_ts).toDS())
//  val dfResult =  df.select("result").first()
//
//  println(dfResult(0))

//  def jsonWrite(): Unit = {
//    //df.write.mode(SaveMode.Overwrite).json("hdfs://data-srv:9000/user/hive/warehouse/testing/ep_test.json")
//    df.select("result").coalesce(1).write.mode(SaveMode.Overwrite).json("data/testing/ep_test.json")
//  }

}
