--  가장 최근의 so2 가져오기
select *
  from (select   data_time
               , sido_name
               , station_name
               , mang_name
               , so2_value
               , row_number() over (partition by sido_name, station_name, mang_name order by data_time desc) rn
          from public.measure_info_real_stage
         where station_name = '논현') a
 where a.rn = 1
;