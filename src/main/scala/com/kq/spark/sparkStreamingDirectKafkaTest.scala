package com.kq.spark

import java.util

import kafka.utils.{ZKGroupTopicDirs, ZkUtils}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.TopicPartition
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.zookeeper.{ZooDefs, ZooKeeper}
import org.apache.zookeeper.data.ACL

import scala.collection.JavaConversions
import scala.collection.mutable.{ArrayBuffer, ListBuffer}

object sparkStreamingDirectKafkaTest {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .master("yarn")
      .appName("sparkStreamingDirectKafkaTest")
      .getOrCreate()

    val ssc = new StreamingContext(spark.sparkContext,Seconds(10))
    val kafkaParms = getkafkaParms()
    val topicSeq = Seq[String]()
    topicSeq.+("rc_jxl_n")
    val zkutils = zkConnInstance("",30000,30000)
    val fromOffsets = readOffSet(zkutils,topicSeq,"group")
    val message: InputDStream[ConsumerRecord[String,String]] = KafkaUtils.createDirectStream(
          ssc,
          LocationStrategies.PreferConsistent,
          ConsumerStrategies.Assign(fromOffsets.keys.toList,kafkaParms,fromOffsets))

    message.map(_.value()).foreachRDD(rdd => {
      val offsetRange = rdd.asInstanceOf[HasOffsetRanges].offsetRanges

      persistOffset(zkutils,offsetRange,"group",true)
//      val result =
    })
//    val inputDStream = KafkaUtils.createDirectStream(ssc,LocationStrategies.PreferConsistent,ConsumerStrategies.Subscribe[String,String](topicSet,kafkaParms))
  }

  def getkafkaParms(): collection.Map[String,Object] = {
    val kafkaParam = Map[String,Object]()
    val brokers ="127.0.0.1"
    kafkaParam.+( "metadata.broker.list" -> brokers )
    kafkaParam.+( "bootstrap.servers" -> brokers )
    //    kafkaParam.put( "group.id" , "dyccallstreaming" )
    kafkaParam.+( "group.id" -> "callProcineDistStreaming01" )
    kafkaParam.+( "max.partition.fetch.bytes" -> "20000000" )
    kafkaParam.+( "key.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer" )
    kafkaParam.+( "value.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer" )
    kafkaParam
  }

//  def saveOffsets(TOPIC_NAME:String,GROUP_ID:String,offsetRanges:Array[OffsetRange],
//                  hbaseTableName:String,batchTime: org.apache.spark.streaming.Time) = {
//    HbaseClientObj.getInstance().init(hbaseTableName)
//    val rowKey = TOPIC_NAME + ":" + GROUP_ID + ":" +String.valueOf(batchTime.milliseconds)
//    for (offset <- offsetRanges) {
//      offset.topic
//      offset.partition
//      offset.untilOffset
//      val arr = ArrayBuffer[(String,AnyRef)]()
//      arr += (("offset",offset.untilOffset.toString))
//      HbaseClientObj.getInstance().put(rowKey,"offset",arr)
//    }
//  }

  def zkConnInstance(zkUrl:String,sessionTimeOut:Int,connectionTimeOut:Int):ZkUtils = {
    val zkClientAndConnection = ZkUtils.createZkClientAndConnection(zkUrl,sessionTimeOut,connectionTimeOut)
    val zkUtils = new ZkUtils(zkClientAndConnection._1,zkClientAndConnection._2,false)
    zkUtils
  }

  def readOffSet(zkutils:ZkUtils ,topics:Seq[String],groupId:String):Map[TopicPartition,Long] = {
    val topicPartOffsetMap = collection.mutable.HashMap.empty[TopicPartition,Long]
    val partitionMap = zkutils.getPartitionsForTopics(topics)
    partitionMap.foreach(topicPartitions => {
      val zkGroupTopicDirs = new ZKGroupTopicDirs(groupId,topicPartitions._1)
      topicPartitions._2.foreach(partition => {
        val offsetPath = zkGroupTopicDirs.consumerOffsetDir + "/" + partition

        try {
          val offsetStatTuple = zkutils.readData(offsetPath)
          if (offsetStatTuple != null) {
            topicPartOffsetMap.put(new TopicPartition(topicPartitions._1,Integer.valueOf(partition)),offsetStatTuple._1.toLong)
          }
        }catch {
          case e : Exception =>
            topicPartOffsetMap.put(new TopicPartition(topicPartitions._1, Integer.valueOf(partition)), 0L)
        }
      })
    })
    topicPartOffsetMap.toMap
  }

  def persistOffset(zkutils:ZkUtils , offsets: Seq[OffsetRange],groupId:String,storeEndOffset:Boolean): Unit ={
    offsets.foreach(or => {
      val zkGroupTopicDirs = new ZKGroupTopicDirs(groupId,or.topic)
      val acls = new ListBuffer[ACL]()
      val acl = new ACL
      acl.setId(ZooDefs.Ids.ANYONE_ID_UNSAFE )
      acl.setPerms(ZooDefs.Perms.ALL)

      acls += acl
      val offsetPath = zkGroupTopicDirs.consumerOffsetDir + "/" + or.partition;
      val offsetVal = if (storeEndOffset) or.untilOffset else or.fromOffset
      zkutils.updatePersistentPath(zkGroupTopicDirs.consumerOffsetDir + "/"
        + or.partition, offsetVal + "", JavaConversions.bufferAsJavaList(acls))
    })
  }

}
