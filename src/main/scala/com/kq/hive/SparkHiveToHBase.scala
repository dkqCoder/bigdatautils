package com.kq.hive

import org.apache.spark.sql.SparkSession
import unicredit.spark.hbase.{HBaseConfig, _}

/**
  * create user: keqiang.du
  * desc: spark 读写 HIVE 的基础操作
  */
object SparkHiveToHBase {
  def main(args: Array[String]): Unit = {
    // 设置提交任务的用户
    System.setProperty("user.name", "root");

    @transient implicit val config = HBaseConfig()
    val warehouseLocation = "file:${system:user.dir}/spark-warehouse"
    val spark = SparkSession
      .builder()
      .appName("SparkHiveToHBase")
      .config("spark.sql.warehouse.dir", warehouseLocation)
      .enableHiveSupport()
      .getOrCreate()

    /**
      * tableName: wahaha
      * columnName: wa, ha, hah
      */
    val sql = "select * from wahaha"
    val resultDataFrame = spark.sql(sql)
    // 将 dataFrame 转成 RDD
    val resultRDD = resultDataFrame.rdd
    // resultRDD 保存到 HBase
    val resultRDDTransfom = resultRDD.map({
      case (row) =>
        val col1val = row.getAs("wa").toString
        val col2val = row.getAs("ha").toString
        val col3val = row.getAs("hah").toString
        val content = Map(
          "hcol1" -> col1val, "hcol2" -> col2val, "hcol2" -> col3val
        )
        "rowKey" -> content
    }).toHBase("hbase_table_name","info")
  }
}
