package com.fullstack.back.model;

import lombok.Data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Id;

@Data
@Entity
@Table(name = "MEASURE_INFO_REAL")
public class MeasureInfoReal implements Serializable
{
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id") private Long id;
    
    @Column(name="data_time") private String datatime;
    @Column(name="sido_name") private String sidoname;
    @Column(name="station_name") private String stationname;
    @Column(name="mang_name") private String mangname;
    @Column(name="so2_value") private String so2value;
    @Column(name="co_value") private String covalue;
    @Column(name="o3_value") private String o3value;
    @Column(name="no2_value") private String no2value;
    @Column(name="pm10_value") private String pm10value;
    @Column(name="pm10_value_24h") private String pm10value24;
    @Column(name="pm25_value") private String pm25value;
    @Column(name="pm25_value_24h") private String pm25value24;
    @Column(name="khai_value") private String khaivalue;
    @Column(name="so2_grade") private String so2grade;
    @Column(name="co_grade") private String cograde;
    @Column(name="o3_grade") private String o3grade;
    @Column(name="no2_grade") private String no2grade;
    @Column(name="pm10_grade") private String pm10grade;
    @Column(name="pm10_grade_1h") private String pm10grade1h;
    @Column(name="pm25_grade") private String pm25grade;
    @Column(name="pm25_grade_1h") private String pm25grade1h;
    @Column(name="khai_grade") private String khaigrade;
    
}
