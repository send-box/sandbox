package com.fullstack.back.model.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeasureInfoRealJson
{
	@JsonProperty("id") private Long id;
	@JsonProperty("data_time") private String datatime;
	@JsonProperty("sido_name") private String sidoname;
	@JsonProperty("station_name") private String stationname;
	@JsonProperty("mang_name") private String mangname;
	@JsonProperty("so2_value") private String so2value;
	@JsonProperty("co_value") private String covalue;
	@JsonProperty("o3_value") private String o3value;
	@JsonProperty("no2_value") private String no2value;
	@JsonProperty("pm10_value") private String pm10value;
	@JsonProperty("pm10_value_24h") private String pm10value24;
	@JsonProperty("pm25_value") private String pm25value;
	@JsonProperty("pm25_value_24h") private String pm25value24;
	@JsonProperty("khai_value") private String khaivalue;
	@JsonProperty("so2_grade") private String so2grade;
	@JsonProperty("co_grade") private String cograde;
	@JsonProperty("o3_grade") private String o3grade;
	@JsonProperty("no2_grade") private String no2grade;
	@JsonProperty("pm10_grade") private String pm10grade;
	@JsonProperty("pm10_grade_1h") private String pm10grade1h;
	@JsonProperty("pm25_grade") private String pm25grade;
	@JsonProperty("pm25_grade_1h") private String pm25grade1h;
	@JsonProperty("khai_grade") private String khaigrade;
}
