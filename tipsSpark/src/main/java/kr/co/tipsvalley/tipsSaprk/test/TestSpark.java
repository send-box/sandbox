package kr.co.tipsvalley.tipsSaprk.test;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;

public final class TestSpark
{
    public static void main(String[] args) throws Exception 
    {
        String appName = "TestSpark";
	    String master  = "local[*]";
	
	    System.setProperty("hadoop.home.dir", "C:\\Hadoop");
	    
        SparkConf sparkConf = new SparkConf().setAppName(appName);
        
        sparkConf.setMaster(master);
    
        SparkContext sc    = new SparkContext(sparkConf);
        SparkSession spark = new SparkSession(sc);
    
        Dataset<Row> myRange = spark.range(1000).toDF("number");
    
        System.out.println("myRange.count(): " + myRange.count());
    
        Dataset<Row> myCsv = spark.read().csv("C:\\Workspace\\test.csv");
    
        System.out.println("myCsv.count(): " + myCsv.count());
    
        StructType type = myCsv.schema();
    
        System.out.println("type.fieldNames().length: " + type.fieldNames().length);
    
        for (String fields : type.fieldNames()) 
        {
    	    System.out.println("fields: " + fields);
        }
      
      //spark.read().json("");
    
        System.out.println("Reached the end.");
    
        spark.close();
    }
}
