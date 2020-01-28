package com.fullstack.batch.mapper;

public class ColumnMap
{
    public static final String[] json = 
    {
          "dataTime"    , "dataTerm"   , "sidoName"    , "stationCode"  , "stationName"  , "mangName"
        , "so2Value"    , "coValue"    , "o3Value"     , "no2Value"     , "pm10Value"    , "pm10Value24"    , "pm25Value"   , "pm25Value24"    , "khaiValue"
        , "so2Grade"    , "coGrade"    , "o3Grade"     , "no2Grade"     , "pm10Grade"    , "pm10Grade1h"    , "pm25Grade"   , "pm25Grade1h"    , "khaiGrade"
        , "serviceKey"  , "pageNo"     , "numOfRows"   , "resultCode"   , "resultMsg"    , "rnum"           , "totalCount"  , "ver"        
    };
    
    public static final String[] column = 
    {
          "data_time"   , "data_term"  , "sido_name"   , "station_code" , "station_name" , "mang_name"
        , "so2_value"   , "co_value"   , "o3_value"    , "no2_value"    , "pm10_value"   , "pm10_value_24h" , "pm25_value"  , "pm25_value_24h" , "khai_value"
        , "so2_grade"   , "co_grade"   , "o3_grade"    , "no2_grade"    , "pm10_grade"   , "pm10_grade_1h"  , "pm25_grade"  , "pm25_grade_1h"  , "khai_grade"
        , "service_key" , "page_no"    , "num_of_rows" , "result_code"  , "result_msg"   , "rnum"           , "total_count" , "ver"        
    };
}
