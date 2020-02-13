package kr.co.tipsvalley.tipsSaprk;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;

import scala.Tuple2;

public class KafkaStreamingTry {
    static Map<String, Object> kafkaParams = new HashMap<>();


    public static void main(String[] args) {
        // Create a local StreamingContext with two working thread and batch interval of 1 second
    	
    	System.out.println("start");
    	
    	String appName = "sparkTestApp";
    	String master = "spark://sparksa1:7077";
    	
    	System.setProperty("hadoop.home.dir", "C:\\spark");
    	
//		try {
			SparkConf conf = new SparkConf().setMaster(master).setAppName(appName);
			JavaStreamingContext jssc = new JavaStreamingContext(conf, new Duration(15000));
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			System.out.println("JavaStreamingContext finally");
//		}
        
        System.out.println("end2");

        kafkaParams.put("bootstrap.servers", "kafka1:9092,kafka2:9092,kafka3:9092");
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("value.deserializer", StringDeserializer.class);
        kafkaParams.put("group.id", "perter-consumer3");
        kafkaParams.put("auto.offset.reset", "latest"); // from-beginning?
        kafkaParams.put("enable.auto.commit", false);

        Collection<String> topics = Arrays.asList("tips_demo_sensor1");

        final JavaInputDStream<ConsumerRecord<String, String>> stream =
                KafkaUtils.createDirectStream(
                        jssc,
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.<String, String>Subscribe(topics, kafkaParams)
                );


        System.out.println("Direct Stream created? ");
//        stream.mapToPair(
//                new PairFunction<ConsumerRecord<String, String>, String, String>() {
//                    @Override
//                    public Tuple2<String, String> call(ConsumerRecord<String, String> record) throws Exception {
//                        System.out.println("record key : "+record.key()+" value is : "+record.value());
//                        return new Tuple2<>(record.key(), record.value());
//                    }
//                });
        
        JavaPairDStream<String, String> jPairDStream =  stream.mapToPair(
                new PairFunction<ConsumerRecord<String, String>, String, String>() {
                    @Override
                    public Tuple2<String, String> call(ConsumerRecord<String, String> record) throws Exception {
                    	System.out.println("record.key() : " + record.key());
                        return new Tuple2<>(record.key(), record.value());
                    }
                });

        jPairDStream.print();
        
        jPairDStream.foreachRDD(jPairRDD -> {
               jPairRDD.foreach(rdd -> {
                    System.out.println("key="+rdd._1()+" value="+rdd._2());
                    System.out.println(rdd.toString());
                });
            });

        try {
        	jssc.start();
        	jssc.awaitTermination();
        } catch (InterruptedException e) {
        	System.out.println("InterruptedException occurrs.");
        }

        System.out.println("Reached the end.");
    }
}