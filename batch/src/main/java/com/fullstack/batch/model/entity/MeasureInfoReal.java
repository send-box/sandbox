package com.fullstack.batch.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table
public class MeasureInfoReal implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id; 

    @Column(name = "data_time"     ) private String columnA1;
  //@Column(name = "data_term"     ) private String columnA2;
    @Column(name = "sido_name"     ) private String columnA3;
  //@Column(name = "station_code"  ) private String columnA4;
    @Column(name = "station_name"  ) private String columnA5;
    @Column(name = "mang_name"     ) private String columnA6;
    @Column(name = "so2_value"     ) private String columnA7;
    @Column(name = "co_value"      ) private String columnA8;
    @Column(name = "o3_value"      ) private String columnA9;
    @Column(name = "no2_value"     ) private String columnB1;
    @Column(name = "pm10_value"    ) private String columnB2;
    @Column(name = "pm10_value_24h") private String columnB3;
    @Column(name = "pm25_value"    ) private String columnB4;
    @Column(name = "pm25_value_24h") private String columnB5;
    @Column(name = "khai_value"    ) private String columnB6;
    @Column(name = "so2_grade"     ) private String columnB7;
    @Column(name = "co_grade"      ) private String columnB8;
    @Column(name = "o3_grade"      ) private String columnB9;
    @Column(name = "no2_grade"     ) private String columnC1;
    @Column(name = "pm10_grade"    ) private String columnC2;
    @Column(name = "pm10_grade_1h" ) private String columnC3;
    @Column(name = "pm25_grade"    ) private String columnC4;
    @Column(name = "pm25_grade_1h" ) private String columnC5;
    @Column(name = "khai_grade"    ) private String columnC6;
  //@Column(name = "service_key"   ) private String columnC7;
  //@Column(name = "page_no"       ) private String columnC8;
  //@Column(name = "num_of_rows"   ) private String columnC9;
  //@Column(name = "result_code"   ) private String columnD1;
  //@Column(name = "result_msg"    ) private String columnD2;
  //@Column(name = "rnum"          ) private String columnD3;
  //@Column(name = "total_count "  ) private String columnD4;
  //@Column(name = "ver"           ) private String columnD5;

    //@Column(insertable=false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    //private String createDate;
}
    