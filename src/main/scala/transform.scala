import io.circe.parser.parse
import io.circe.{Decoder, HCursor, Json}
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession


object transform {
  val session: SparkSession = sparkCluster.spark
  val sc: SparkContext = sparkCluster.spark.sparkContext

  def dataTransform(): Map[String, String] = {
    val apiMap = extract.apiCalls()
    val ts = apiMap("total_supply")
    val ep = apiMap("eth_price")
    val ns = apiMap("node_size")
    val nc = apiMap("node_count")
    val go = apiMap("gas_oracle")

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

      val eb_json: Decoder.Result[String] = {
        ep_cursor.downField("result").downField("ethbtc").as[String]
      }
      val eu_json: Decoder.Result[String] = {
        ep_cursor.downField("result").downField("ethusd").as[String]
      }
      val time_json: Decoder.Result[String] = {
        ep_cursor.downField("result").downField("ethbtc_timestamp").as[String]
      }

      val eb_result: String = eb_json.productElement(0).toString
      val eu_result: String = eu_json.productElement(0).toString
      val time_result: String = time_json.productElement(0).toString
    }

    class nodeSize {
      val ns_curled: Json = parse(ns).getOrElse(Json.Null)
      val ns_cursor: HCursor = ns_curled.hcursor

      val bn_json: Decoder.Result[String] = {
        ns_cursor.downField("result").downArray.downField("blockNumber").as[String]
      }
      val cts_json: Decoder.Result[String] = {
        ns_cursor.downField("result").downArray.downField("chainTimeStamp").as[String]
      }
      val cs_json: Decoder.Result[String] = {
        ns_cursor.downField("result").downArray.downField("chainSize").as[String]
      }

      val bn_result: String = bn_json.productElement(0).toString
      val cts_result: String = cts_json.productElement(0).toString
      val cs_result: String = cs_json.productElement(0).toString
    }

    class nodeCount {
      val nc_curled: Json = parse(nc).getOrElse(Json.Null)
      val nc_cursor: HCursor = nc_curled.hcursor

      val utc_json: Decoder.Result[String] = {
        nc_cursor.downField("result").downField("UTCDate").as[String]
      }
      val nc_json: Decoder.Result[String] = {
        nc_cursor.downField("result").downField("TotalNodeCount").as[String]
      }

      val utc_result: String = utc_json.productElement(0).toString
      val nc_result: String = nc_json.productElement(0).toString
    }

    class gasOracle {
      val go_curled: Json = parse(go).getOrElse(Json.Null)
      val go_cursor: HCursor = go_curled.hcursor

      val go_safe_json: Decoder.Result[String] = {
        go_cursor.downField("result").downField("SafeGasPrice").as[String]
      }
      val go_med_json: Decoder.Result[String] = {
        go_cursor.downField("result").downField("ProposeGasPrice").as[String]
      }
      val go_fast_json: Decoder.Result[String] = {
        go_cursor.downField("result").downField("FastGasPrice").as[String]
      }
      val go_base_fee: Decoder.Result[String] = {
        go_cursor.downField("result").downField("suggestBaseFee").as[String]
      }

      val result_safe: String = go_safe_json.productElement(0).toString
      val result_med: String = go_med_json.productElement(0).toString
      val result_fast: String = go_fast_json.productElement(0).toString
      val result_fee: String = go_base_fee.productElement(0).toString
    }

    val ts_obj = new totalSupply
    val ep_obj = new ethereumPrice
    val ns_obj = new nodeSize
    val nc_obj = new nodeCount
    val go_obj = new gasOracle

    object resultMap {
      val epMap: Map[String, String] = Map("ethbtc" -> ep_obj.eb_result, "ethusd" -> ep_obj.eu_result, "timestamp" -> ep_obj.time_result)
      val nsMap: Map[String, String] = Map("blocknumber" -> ns_obj.bn_result, "chaintimestamp" -> ns_obj.cts_result, "chainsize" -> ns_obj.cs_result)
      val ncMap: Map[String, String] = Map("utc" -> nc_obj.utc_result, "nodecount" -> nc_obj.nc_result)
      val goMap: Map[String, String] = Map("safeprice" -> go_obj.result_safe, "suggestedprice" -> go_obj.result_med, "fastprice" -> go_obj.result_fast, "basefee" -> go_obj.result_fee)

      val rMap: Map[String, Map[String, String]] = Map("pricemap" -> epMap, "sizemap" -> nsMap, "countmap" -> ncMap, "gasmap" -> goMap)
    }

    object dataMap {
      val aggMap: Map[String, String] = Map(
        "totalsupply" -> ts_obj.ts_result,
        "ethbtc" -> resultMap.rMap("pricemap")("ethbtc"),
        "ethusd" -> resultMap.rMap("pricemap")("ethusd"),
        "pricetime" -> resultMap.rMap("pricemap")("timestamp"),
        "blockheight" -> resultMap.rMap("sizemap")("blocknumber"),
        "chaintime" -> resultMap.rMap("sizemap")("chaintimestamp"),
        "chainsize" -> resultMap.rMap("sizemap")("chainsize"),
        "utctime" -> resultMap.rMap("countmap")("utc"),
        "nodecount" -> resultMap.rMap("countmap")("nodecount"),
        "safeprice" -> resultMap.rMap("gasmap")("safeprice"),
        "suggested" -> resultMap.rMap("gasmap")("suggestedprice"),
        "fastprice" -> resultMap.rMap("gasmap")("fastprice"),
        "basefee" -> resultMap.rMap("gasmap")("basefee")
      )
    }

    dataMap.aggMap

  }
}
