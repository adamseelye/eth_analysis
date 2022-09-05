import scala.io.StdIn.readLine
import scala.language.postfixOps
import sys.process._

object apiObject {
  class apiCalls {
    val api_token = ""

    val api_map: Map[String, String] = Map(
      "total_supply" -> f"https://api.etherscan.io/api?module=stats&action=ethsupply&apikey=${api_token}",
      "eth_price" -> f"https://api.etherscan.io/api?module=stats&action=ethprice&apikey=${api_token}",
      "node_size" -> f"https://api.etherscan.io/api?module=stats&action=chainsize&startdate=2019-02-01&enddate=2019-02-28&clienttype=geth&syncmode=default&sort=asc&apikey=${api_token}"
    )

    val curl_ts: String = s"curl ${api_map("total_supply")}" !!
    val curl_ep: String = s"curl ${api_map("eth_price")}" !!
    val curl_ns: String = s"curl ${api_map("node_size")}" !!

    println("1. Ethereum Total Supply")
    println("2. Ethereum Prices, BTC and USD")
    println("3. Ethereum Nodes")

    val whichAPI: String = readLine {
      "Please choose which query to make on Ethereum: "
    }

  }

}
