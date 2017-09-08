package day0831
import org.apache.spark.sql.{Row, SparkSession}
import unicredit.spark.hbase._
import unicredit.spark.hbase.HBaseConfig
object Test003 {
  def main(args: Array[String]): Unit = {
    val warehouseLocation = "file:${system:user.dir}/spark-warehouse"

    val spark = SparkSession
      .builder
      .appName("HotelGuessLike")
      .config("spark.sql.warehouse.dir", warehouseLocation)
      .enableHiveSupport()
      .getOrCreate()

    val sc = spark.sparkContext
    spark.sparkContext.setLogLevel("ERROR")

    @transient implicit val config = HBaseConfig()
    val travelGroupCloumn = Map(
      "info" -> Set("LINE_ID","LEFT_NUM")
    )

    val testRdd = sc.hbase[String]("hbase-table",travelGroupCloumn)

  }
}
