package kr.co.tipsvalley.tipsSaprk;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class LineCount
{
    public static void main(String[] args)
    {
        System.setProperty("hadoop.home.dir", "C:\\Hadoop");
        
        SparkConf conf = new SparkConf().setAppName("LineCount");
        
        JavaSparkContext ctx = new JavaSparkContext(conf);
        
        JavaRDD<String> textLoadRDD = ctx.textFile("C:\\spark-3.0.0-preview2-bin-hadoop2.7\\README.md");

        System.out.println("line cnt: " + textLoadRDD.count());
    }
}