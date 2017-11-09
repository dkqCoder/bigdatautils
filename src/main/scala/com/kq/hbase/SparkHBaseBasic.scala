package com.kq.hbase

import javax.jdo.annotations.Value

import org.apache.spark.sql.SparkSession
import unicredit.spark.hbase.{HBaseConfig, _}

/**
  * 基于开源的 hbase-rdd 项目 实现的spark读写hbase的demo
  * 附上 hbase-rdd github地址：https://github.com/unicredit/hbase-rdd
  */
object SparkHBaseBasic {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("SparkHBase")
      .getOrCreate()

    val sc = spark.sparkContext
    sc.setLogLevel("ERROR")

    @transient implicit val config = HBaseConfig()

    /**
      * HBase 表列名组成一个map集合
      */
    val columnMap = Map(
      "cf" -> Set("keyvalue_create_at","keyvalue_create_user","keyvalue_key","keyvalue_memo","keyvalue_status","keyvalue_update_at","keyvalue_update_user","keyvalue_value")
    )

    /**
      * 获取HBase 表 read_hbase_table_name 中 columnMap 集合中的字段的数据后转成一个RDD
      */
    val rdd = sc.hbase[String]("read_hbase_table_name",columnMap).map(_._2("cloumFamily")("cloumName"))
    val rddCount = sc.hbase[String]("read_hbase_table_name",columnMap).count()

    /**
      * 数据保存HBase 表 save_hbase_table_name 中
      */
    val saveHbaseRdd = rdd.map({
      case (vals) =>
        val content =Map("user" -> vals)
        "rowKey" -> content
    }).toHBase("save_hbase_table_name","info")

  }
}
