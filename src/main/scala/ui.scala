import scala.io.StdIn.readLine

object ui {
  println("1. Ethereum Total Supply")
  println("2. Ethereum Prices, BTC and USD")
  println("3. Ethereum Nodes")

  val whichAPI = readLine("Please choose which query to make on Ethereum: ")

}
