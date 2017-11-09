package com.kq.elasticsearch

import org.apache.spark.{SparkConf, SparkContext}
import org.elasticsearch.spark._
import org.elasticsearch.spark.rdd.EsSpark

object SparkEsBasicDemo {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    val sc = new SparkContext(conf)

    val number = Map("one" -> "1","two" -> 2,"three" -> 3)
    val airports = Map("arrival" -> "Otopeni", "SFO" -> "San Fran")

    sc.makeRDD(Seq(number,airports)).saveToEs("spark/docs")

    val upcomingTrip = Trip("OTP","SFO")
    val lastWeekTrip = Trip("MUC","OTP")

    val rdd = sc.makeRDD(Seq(upcomingTrip,lastWeekTrip))
    EsSpark.saveToEs(rdd,"spark/docs")

    val json1 = """{"reason" : "business", "airport" : "SFO"}"""
    val json2 = """{"participants" : 5, "airport" : "OTP"}"""

    sc.makeRDD(Seq(json1,json2)).saveJsonToEs("spark/json-trips")

    // reading data from ES
    val esRdd = sc.esRDD("radio/artists")
  }

  case class Trip(departure: String, arrival: String)
}
