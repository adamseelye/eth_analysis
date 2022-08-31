object sparkTest extends App {
  val session = sparkCxn.spark
  val sc = session.sparkContext

  val testTable = session.sql("CREATE TABLE testTable (`` string, `status` string, `message` string, `result` string) " +
                                        "ROW FORMAT DELIMITED " +
                                        "FIELDS TERMINATED BY ',' " +
                                        "tblproperties('skip.header.line.count'='1') " +
                                        "LINES TERMINATED BY '\n' " +
                                        "STORED AS TEXTFILE;")

  val testLoad = session.sql("LOAD DATA INPATH '/home/debian/scala_practice/eth_analysis/data/total_supply.csv' OVERWRITE INTO TABLE testTable;")

  val testSelect = session.sql("select * from testTable;")

//  val dropSQL = session.sql("drop table testtable;")
//
//  val showTables = session.sql("show tables;")

  testSelect.show()
  //showTables.show()


}
