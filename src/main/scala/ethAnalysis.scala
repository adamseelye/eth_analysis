import scala.io.StdIn.readLine

object ethAnalysis extends App {
  println("1. Ethereum Total Supply")
  println("2. Ethereum Prices")
  println("3. Ethereum Chain Data")
  println("4. Ethereum Node Count")
  println("5. Gas Prices")
  println("6. Save CSV data files")
  println("7. Save JSON data files")
  println("8. Read CSV data files")
  println("9. Read JSON data files")

  val dataRead = readLine("Please choose a menu item: ")

  val session = sparkCluster.spark
  val hdfsPath = "hdfs://data-srv:9000/user/hive/warehouse/eth_analysis"

  if (dataRead == "1") {
    load.tsFrame().show()
  } else if (dataRead == "2") {
    load.epFrame().show()
  } else if (dataRead == "3") {
    load.nsFrame().show()
  } else if (dataRead == "4") {
    load.ncFrame().show()
  } else if (dataRead == "5") {
    load.goFrame().show()
  } else if (dataRead == "6") {
    load.writeCSV()
  } else if (dataRead == "7") {
    load.writeJSON()
  } else if (dataRead == "8") {
    session.read.format("csv").option("header", "true").load(s"$hdfsPath/nscsv").show()
    println("Ethereum Total Supply (Gwei)")
  } else if (dataRead == "9") {
    session.read.format("json").load(s"$hdfsPath/nsjson").show()
    println("Ethereum Total Supply (Gwei)")
  } else {
    println("Error")
    sys.exit(1)
  }

}
