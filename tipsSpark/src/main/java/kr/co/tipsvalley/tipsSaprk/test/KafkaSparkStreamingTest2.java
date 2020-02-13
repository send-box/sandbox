package kr.co.tipsvalley.tipsSaprk.test;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.ForeachWriter;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.streaming.StreamingQueryListener;
import org.apache.spark.sql.streaming.Trigger;
import org.apache.spark.sql.types.ObjectType;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;

import scala.Tuple2;

public final class KafkaSparkStreamingTest2 {
	public static void main(String[] args) throws Exception {
		String brokers = "kafka1:9092,kafka2:9092,kafka3:9092";
//		String groupId = "perter-consumer4";
		String topics = "tips_demo_sensor1";
		String topicsWrite = "demo_sensor_spark_w"; //출력용
		String warehouseDir = "D:\\Work\\Java\\eclipse\\workspace\\tipsSpark\\warehouse";
		String checkpointLocation = "D:\\Work\\Java\\eclipse\\workspace\\tipsSpark\\checkpointLocation";
		
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
				System.out.println("Query made progress: " + arg0.progress());
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
		
		System.out.println(ds1.schema().sql());
		
//		JavaDStream<Order> orders = null;
//		JavaPairDStream<String, Integer> stocksPerWindow = orders.mapToPair((Order x) -> new Tuple2<String, Integer>(x.symbol, x.amount)).
//		    	reduceByKeyAndWindow((Integer a1, Integer a2) -> a1+a2, Durations.minutes(60));
		
		Dataset<Row> ds2 = ds1.withWatermark("timestamp", "1 seconds")
//			.groupBy(new Window())
			.withColumn("array_val", functions.split(new Column("value"), ","))
			.where("array_val[8] > 0")
			.selectExpr("array_val[0] as DEVICE_MAC_ADDR"
				, "array_val[7] as ILLUMINACE"
				, "array_val[8] as TEMPERATURE"
				, "array_val[9] as HUMIDITY"
				, "substring(array_val[10], 0) as DEVICE_TIME"
				, "to_timestamp(timestamp, 'yyyyMMdd') as timestamp")
			.dropDuplicates("timestamp")
//			.selectExpr("max(DEVICE_MAC_ADDR) as DEVICE_MAC_ADDR"
//				, "round(avg(ILLUMINACE), 2) as ILLUMINACE"
//				, "round(avg(TEMPERATURE), 2) as TEMPERATURE"
//				, "round(avg(HUMIDITY), 2) as HUMIDITY"
//			)
			;
		
		System.out.println(ds2.schema().sql());
		
		int idx3 = 0;
		while (idx3 < 45) {
			ds2.writeStream().trigger(Trigger.ProcessingTime(15, TimeUnit.SECONDS))
			.foreach(new ForeachWriter<Row>() {
				private static final long serialVersionUID = 1L;
				
				@Override
				public void process(Row arg0) {
					if(arg0.get(0) != null) {
						System.out.println("DEVICE_MAC_ADDR: " + arg0.get(0).toString());
						System.out.println("ILLUMINACE: " + arg0.getString(1));
//						System.out.println("TEMPERATURE: " + arg0.getDouble(2));
//						System.out.println("HUMIDITY: " + arg0.getDouble(3));
						System.out.println("DEVICE_TIME: " + arg0.getString(4));
						
//						if (arg0.getDouble(1) < 50) {
						if (Double.parseDouble(arg0.getString(1)) < 50) {
							System.out.println("조도값이 50 미만입니다.");
						}
					}
				}
				
				@Override
				public boolean open(long partitionId, long version) {
					System.out.println("partitionId: " + partitionId);
					System.out.println("version: " + version);
					return true;
				}
				
				@Override
				public void close(Throwable arg0) {
//					System.out.println(arg0.getMessage());
				}
			}).outputMode("append").start().awaitTermination(30000);
			
			System.out.println("idx3: " + ++idx3);
//			ds2.show();
			Thread.sleep(3000);
		}
		
		
//		ds2 = ds2.groupBy("DEVICE_MAC_ADDR", "ILLUMINACE").avg();
		
//		ds2.selectExpr("topic", "DEVICE_MAC_ADDR", "ILLUMINACE", "TEMPERATURE", "HUMIDITY")
		
			
//		ds2.writeStream()
//		.trigger(Trigger.ProcessingTime(5, TimeUnit.SECONDS))
//		.queryName("aaaa2").format("memory").outputMode("complete").start();
		
		int idx2 = 0;
		while (idx2 < 45) {
//			spark.sql("SELECT DEVICE_MAC_ADDR, round(avg(TEMPERATURE), 2) as avg_TEMPERATURE, round(avg(ILLUMINACE), 2) as avg_ILLUMINACE, round(avg(HUMIDITY), 2) as avg_HUMIDITY FROM aaaa2 group by DEVICE_MAC_ADDR").show();
//			spark.sql("SELECT DEVICE_MAC_ADDR, round(avg(ILLUMINACE), 2) as avg_ILLUMINACE").show();
//			spark.sql("SELECT * FROM aaaa2").show();
//			spark.sql("SELECT * FROM aaaa3").show();
			System.out.println("idx2: " + ++idx2);
//			ds2.show();
			Thread.sleep(3000);
		}
		
		spark.close();
//		sq.awaitTermination();
		
		System.out.println("end");
	}
	
}