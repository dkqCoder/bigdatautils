package com.kq.hive

import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types.{StringType, StructField, StructType}

object SparkHiveBasic {
  def main(args: Array[String]): Unit = {
    val warehouseLocation = "file:${system:user.dir}/spark-warehouse"
    val spark = SparkSession
      .builder()
      .appName("SparkHiveToHBase")
      .config("spark.sql.warehouse.dir", warehouseLocation)
      .enableHiveSupport()
      .getOrCreate()

    val sc = spark.sparkContext

    val rddCustomers = sc.textFile("path")

    val schemaString = "customer_id,name,city,state,zip_code"

    val schema = StructType(schemaString.split(",").map(fieldName => StructField(fieldName,StringType,true)))

    val rowRDD = rddCustomers.map(_.split(",")).map(p => Row(p(0).trim,p(1),p(2),p(3),p(4)))

    val dfCustomers = spark.sqlContext.createDataFrame(rowRDD,schema)

    dfCustomers.createOrReplaceTempView("customers")

  }
}
