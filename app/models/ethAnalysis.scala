package models

import scala.io.StdIn.readLine

object ethAnalysis extends App {
  println("1. Ethereum Total Supply")
  println("2. Ethereum Prices")
  println("3. Ethereum Chain Data")
  println("4. Ethereum Node Count")
  println("5. Gas Prices")

  val dataRead = readLine("Please choose which data to view: ")

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
  }

}
