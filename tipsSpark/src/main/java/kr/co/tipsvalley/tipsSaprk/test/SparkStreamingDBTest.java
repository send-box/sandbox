package kr.co.tipsvalley.tipsSaprk.test;

import java.util.Properties;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.streaming.StreamingQueryListener;

public final class SparkStreamingDBTest {
	public static void main(String[] args) throws Exception {
		String brokers = "kafka1:9092,kafka2:9092,kafka3:9092";
		String topics = "tips_demo_sensor1";
		String topicsWrite = "demo_sensor_spark_w"; //출력용
		String warehouseDir = "C:\\eclipse\\workspace\\tipsSpark\\warehouse";
		String checkpointLocation = "C:\\eclipse\\workspace\\tipsSpark\\checkpointLocation";
		
		String appName = "KafkaSparkStreamingTest";
//		String master = "spark://sparksa1:7077";
		String master = "local[*]";
	
		SparkConf sparkConf = new SparkConf().setAppName(appName);
		sparkConf.setMaster(master);
		
		SparkContext sc = SparkContext.getOrCreate(sparkConf);
		SparkSession spark = new SparkSession(sc);
		
//		SparkSession spark = SparkSession.builder()
//				  .appName(appName)
//				  .master(master)
//				  .config("spark.sql.warehouse.dir", warehouseDir)
//				  .getOrCreate();
		spark.sparkContext().setLogLevel("ERROR");
		
		spark.conf().set("spark.sql.shuffle.partitions", 1); // default: 200
		
		// 모니터링
		spark.streams().addListener(new StreamingQueryListener() {
			@Override
			public void onQueryProgress(QueryProgressEvent arg0) {
//				System.out.println("Query made progress: " + arg0.progress());
				System.out.println("arg0.progress().numInputRows(): " + arg0.progress().numInputRows());
			}

			@Override
			public void onQueryStarted(QueryStartedEvent arg0) {
				System.out.println("Query made started: " + arg0.id());
			}

			@Override
			public void onQueryTerminated(QueryTerminatedEvent arg0) {
				System.out.println("Query made terminated: " + arg0.id());
			}
		});
		
		Dataset<Row> ds1 = spark.readStream()
		.format("kafka")
		.option("kafka.bootstrap.servers", brokers)
		.option("subscribe", topics)
		.load();
		
		System.out.println("ds1.schema().sql(): " + ds1.schema().sql());
		
		Dataset<Row> ds2 = ds1.withWatermark("timestamp", "10 seconds")
			.withColumn("array_val", functions.split(new Column("value"), ","))
			.where("array_val[8] > 0")
			.selectExpr("array_val[0] as DEVICE_MAC_ADDR"
				, "array_val[7] as ILLUMINACE"
				, "array_val[8] as TEMPERATURE"
				, "array_val[9] as HUMIDITY"
				, "substring(array_val[10], 0) as DEVICE_TIME"
				, "to_timestamp(timestamp, 'yyyyMMdd') as timestamp"
			)
			.dropDuplicates("timestamp");
		
		System.out.println("ds2.schema().sql(): " + ds2.schema().sql());
		
		
//		ds2.writeStream().trigger(Trigger.ProcessingTime(5, TimeUnit.SECONDS)).queryName("qry")
//		.format("memory").outputMode("complete").start();
//		
//		ds2.withWatermark("timestamp", "10 seconds").selectExpr("ILLUMINACE ||','|| TEMPERATURE ||','|| HUMIDITY||','||DEVICE_TIME  as value")
//		.writeStream().trigger(Trigger.ProcessingTime(5, TimeUnit.SECONDS)).format("console").outputMode("complete").start();
//		spark.sql("select * from qry").show();
		
		String url = "jdbc:sap://192.168.1.118:30115";
		String table = "SPARK_SENSOR_DATA";
		Properties connectionProperties = new Properties();
		connectionProperties.put("driver", "com.sap.db.jdbc.Driver");
		connectionProperties.put("user", "SYSTEM");
		connectionProperties.put("password", "Welcome1");
		
		ds2
		.groupBy("DEVICE_MAC_ADDR")
		.agg(functions.expr("max(DEVICE_TIME) as DEVICE_TIME")
			, functions.expr("round(avg(ILLUMINACE), 2) as AVG_ILLUMINACE")
			, functions.expr("round(min(ILLUMINACE), 2) as MIN_ILLUMINACE")
			, functions.expr("round(max(ILLUMINACE), 2) as MAX_ILLUMINACE")
			, functions.expr("round(avg(TEMPERATURE), 2) as AVG_TEMPERATURE")
			, functions.expr("round(min(TEMPERATURE), 2) as MIN_TEMPERATURE")
			, functions.expr("round(max(TEMPERATURE), 2) as MAX_TEMPERATURE")
			, functions.expr("round(avg(HUMIDITY), 2) as AVG_HUMIDITY")
			, functions.expr("round(min(HUMIDITY), 2) as MIN_HUMIDITY")
			, functions.expr("round(max(HUMIDITY), 2) as MAX_HUMIDITY")
			, functions.expr("max(timestamp) as timestamp")
		)
		.write().mode("append").jdbc(url, table, connectionProperties);
		
		spark.close();
		
		System.out.println("end");
	}
}