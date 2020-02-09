package com.example.mini.scala

import org.apache.spark._
import org.apache.spark.SparkContext._

object HelloWorld
{
    val conf = new SparkConf().setMaster("local").setAppName("wordCount")  // local을 쓰면 따로 접속할 필요가 없음 나타낸다.
    val sc = new SparkContext(conf)

    def main(args: Array[String]): Unit = {
        val inputRDD = sc.textFile("C:\\Workspace\\ScalaWork\\src\\main\\scala\\sample.txt")

        // Word Count
        //val words = input.flatMap(line => line.split(" "))
        //val counts = words.map(word => (word, 1)).reduceByKey { case (x, y) => x + y }
        //counts.saveAsTextFile("C:\\Workspace\\ScalaWork\\src\\main\\scala\\sample.txt.result")

        //
        val errorRDD = inputRDD.filter(line => line.contains("error"))
        val warningRDD = inputRDD.filter(line => line.contains("warning"))
        var noticeRDD = errorRDD.union(warningRDD)

        println("errorRDD.count() : " + errorRDD.count())
        println("warningRDD.count() : " + warningRDD.count())
        println("noticeRDD.count() : " + noticeRDD.count())

        sys.exit(0)
    }
}
