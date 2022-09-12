package controllers {

import org.apache.spark.sql.SparkSession

import javax.inject._
import play.api.mvc._


class ethController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  val session: SparkSession = models.sparkCluster.session
  val hdfsPath = "hdfs://data-srv:9000/user/hive/warehouse/eth_analysis"

  def tSupply(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val tsString = session.read.format("json").load(s"$hdfsPath/tsjson").first()(0).toString
    val total = "Ethereum Total Supply (Gwei): " + tsString
    Ok(views.html.eth.eth_analysis(total))
  }

  def ePrice(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val epString = session.read.format("json").load(s"$hdfsPath/epjson").collect().mkString
    val price = "Ethereum Price [ETH/BTC, ETH/USD, Timestamp]: " + epString
    Ok(views.html.eth.eth_analysis(price))
  }

  def cStats(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    // Must be loaded as csv, decoding errors with json
    val nsString = session.read.format("csv").option("header", "true").load(s"$hdfsPath/nscsv").first().toString
    val nsize = "Chain Stats: " + nsString
    Ok(views.html.eth.eth_analysis(nsize))
  }

  def nCount(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val ncString = session.read.format("json").load(s"$hdfsPath/ncjson").first()(0).toString
    val ncount = "Node Count: " + ncString
    Ok(views.html.eth.eth_analysis(ncount))
  }

  def gOracle(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    // Must figure this one out later - csv and json decoding errors
    val goString = session.read.format("csv").option("header", "true").load(s"$hdfsPath/gocsv").collectAsList().toString
    val oracle = "Gas Prices: " + goString
    Ok(views.html.eth.eth_analysis(oracle))
  }

}
}
