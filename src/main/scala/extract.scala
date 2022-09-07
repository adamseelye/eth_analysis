import scala.language.postfixOps
import sys.process._


object extract {
  def apiCalls(): Map[String, String] = {
    val apiToken = ""

    val apiMap: Map[String, String] = Map(
      "total_supply" -> f"https://api.etherscan.io/api?module=stats&action=ethsupply&apikey=${apiToken}",
      "eth_price" -> f"https://api.etherscan.io/api?module=stats&action=ethprice&apikey=${apiToken}",
      "node_size" -> f"https://api.etherscan.io/api?module=stats&action=chainsize&startdate=2019-02-01&enddate=2019-02-28&clienttype=geth&syncmode=default&sort=asc&apikey=${apiToken}",
      "node_count" -> f"https://api.etherscan.io/api?module=stats&action=nodecount&apikey=${apiToken}",
      "gas_oracle"-> f"https://api.etherscan.io/api?module=gastracker&action=gasoracle&apikey=${apiToken}"
    )

    val curl_ts: String = s"curl ${apiMap("total_supply")}" !!
    val curl_ep: String = s"curl ${apiMap("eth_price")}" !!
    val curl_ns: String = s"curl ${apiMap("node_size")}" !!
    val curl_nc: String = s"curl ${apiMap("node_count")}" !!
    val curl_go: String = s"curl ${apiMap("gas_oracle")}" !!


    val curlMap: Map[String, String] = Map(
      "total_supply" -> curl_ts,
      "eth_price" -> curl_ep,
      "node_size" -> curl_ns,
      "node_count"-> curl_nc,
      "gas_oracle" -> curl_go
    )

    curlMap

  }
}
