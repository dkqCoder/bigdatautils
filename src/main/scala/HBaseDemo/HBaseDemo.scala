package HBaseDemo

import org.apache.spark.sql.SparkSession
import unicredit.spark.hbase.HBaseConfig
import unicredit.spark.hbase._

object HBaseDemo {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("HBaseDemo")
      .getOrCreate()

    val sc = spark.sparkContext
    sc.setLogLevel("ERROR")

    @transient implicit val config = HBaseConfig()
    println("--------------start--------------")

//    val column = Array("keyvalue_create_at","keyvalue_create_user","keyvalue_key","keyvalue_memo","keyvalue_status","keyvalue_update_at","keyvalue_update_user","keyvalue_value")

    val column = Map(
      "cf" -> Set("keyvalue_create_at","keyvalue_create_user","keyvalue_key","keyvalue_memo","keyvalue_status","keyvalue_update_at","keyvalue_update_user","keyvalue_value")
    )

    val rdd = sc.hbase[String]("riskdata:o_keyvalue",column).map(_._2("cf")("keyvalue_create_user"))
//    val rdd = sc.hbase[String]("riskdata:o_keyvalue",column).count()

    val saveHbaseRdd = rdd.map({
      case (vals) =>
        val content =Map("user" -> vals)

        "rowKey" -> content
    }).toHBase("test_table","info")

  }
}
