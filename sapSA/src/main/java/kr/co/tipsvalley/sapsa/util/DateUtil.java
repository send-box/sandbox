package kr.co.tipsvalley.sapsa.util;

import java.sql.Timestamp;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.DurationFieldType;
import org.joda.time.Weeks;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.google.common.base.Preconditions;

/**
 * DateUtil.java Class title
 * 
 * <p><ul><li>날짜, 시간 및 요일 변환과 관련된 Util 클래스</li></ul>
 *
 * @version 1.0.0
 * @since 2013. 10. 8.
 */
public class DateUtil {
	private static Logger logger = LoggerFactory.getLogger(DateUtil.class);

	public static final String DATE_DEFAULT_PATTERN = "yyyyMMdd";
	
	public static final String DATE_DASH_PATTERN = "yyyy-MM-dd";
	
	public static final String DATE_SLASH_PATTERN = "yyyy/MM/dd";
	
	public static final String TIME_DEFAULT_PATTERN = "HHmmss";
	
	public static final String TIME_COLONE_PATTERN = "HH:mm:ss";
	
	public static final String DATE_HMS_PATTERN = "yyyyMMddHHmmss";
	
	public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	public static final String TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
	
	public static final String TIMESTAMP_PATTERN2 = "yyyy-MM-dd'T'HH:mm:ss.SSS'z'";
	
	public static final String TIMESTAMP_HMS_PATTERN = "yyyyMMddHHmmssSSS";
	

	public static final String DATETIME_HAN_PATTERN = "yyyy년 MM월 dd일 HH시 mm분 ss초";
	
	/**
	 * 현재 날짜를 조회한다.
	 * <p>
	 * ex) 20131001 (yyyyMMdd)
	 *
	 * @return String
	 */
	public static String getCurrentDay() {
		return getCurrentDateTime(DATE_DEFAULT_PATTERN);
	}
	
	/**
	 * 사용자가 입력한 형식에 맞는 현재 날짜를 조회한다
	 *
	 * @param pattern
	 * @return
	 */
	public static String getCurrentDay(String pattern) {
		return getCurrentDateTime(pattern);
	}
	
	/**
	 * 현재 시간을 조회한다.
	 * <p>
	 * ex) 113014 (HHmmss)
	 *
	 * @return
	 */
	public static String getCurrentTime() {
		return getCurrentDateTime(TIME_DEFAULT_PATTERN);
	}
	
	
	/**
	 * 사용자가 입력한 형식에 맞는 현재 시간을 조회한다.
	 *
	 * @param pattern
	 * @return
	 */
	public static String getCurrentTime(String pattern) {
		return getCurrentDateTime(pattern);
	}
	
	/**
	 * 사용자가 입력한 형식에 맞는 현재 날짜, 시간을 조회한다.
	 *
	 * @param format
	 * @return
	 */
	public static String getCurrentDateTime(String format) {
		return new DateTime().toString(DateTimeFormat.forPattern(format));
	}
	
	/**
	 * 현재 년도를 조회한다.
	 *
	 * @return
	 */
	public static String getThisYear() {
		 return getCurrentDateTime("yyyy");
	}
	
	/**
	 * 입력받은 일자의 요일을 반환한다.
	 *
	 * @param date
	 * @return
	 */
	public static String getDayOfWeek(String date) {
		return getDayOfWeek(date, DATE_DEFAULT_PATTERN, true);
	}
	
	/**
	 * 입력받은 일자의 요일을 반환한다.
	 *
	 * @param date
	 * @param pattern 날짜 패턴
	 * @param abbreviation 요일 축약여부
	 * @return
	 */
	public static String getDayOfWeek(String date, String pattern, boolean abbreviation) {
		DateTime dateTime =  DateTimeFormat.forPattern(pattern).parseDateTime(date);
		DateTime.Property dayOfWeek = dateTime.dayOfWeek();

		if (abbreviation)
			return dayOfWeek.getAsShortText(Locale.KOREA);
		else
			return dayOfWeek.getAsText(Locale.KOREA);
	}
	
	/**
	 * 입력받은 두 날짜 사이의 일자를 계산한다.
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getDays(String startDate, String endDate) {
		return getDays(startDate, endDate, DATE_DEFAULT_PATTERN);
	}
	
	/**
	 * 입력받은 두 날짜 사이의 일자를 계산한다.
	 * 
	 * @param startDate
	 * @param endDate
	 * @param pattern
	 * @return
	 */
	public static int getDays(String startDate, String endDate, String pattern) {
		Assert.notNull(startDate, "startDate must be set");
		Assert.notNull(endDate, "endDate must be set");
		
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern);
		
		DateTime startDateTime = dateTimeFormatter.parseDateTime(startDate);
		DateTime endDateTime = dateTimeFormatter.parseDateTime(endDate);
		
		long startMillis = startDateTime.getMillis();
		long endMillis = endDateTime.getMillis();
		
		int startDay = (int) (startMillis / (60 * 60 * 1000 * 24));
		int endDay = (int) (endMillis / (60 * 60 * 1000 * 24));
		
		return endDay - startDay;
	}
	
	/**
	 * 입력받은 일자에 대해서 해당 일만큼 더한 일자를 반환한다. 
	 * <p>
	 * 마이너스 일자는 입력받은 일자보다 이전의 일자로 계산해서 반환한다.
	 *
	 * @param date
	 * @param days
	 * @return
	 */
	public static String addDays(String date, int days) {
		return addDays(date, days, DATE_DEFAULT_PATTERN);
	}
	
	/**
	 * 입력받은 일자에 대해서 해당 일만큼 더한 일자를 반환한다. 
	 * <p>
	 * 마이너스 일자는 입력받은 일자보다 이전의 일자로 계산해서 반환한다.
	 * 
	 * @param date
	 * @param days
	 * @param pattern
	 * @return
	 */
	public static String addDays(String date, int days, String pattern) {
		return addDate(date, 0, 0, days, pattern);
	}
	
	/**
	 * 입력받은 일자에 대해서 해당 개월수만큼 더한 일자 반환한다. 
	 * <p>
	 * 마이너스 개월수는 입력받은 일자보다 이전 일자로 계산해서 반환한다.
	 * 
	 * @param date
	 * @param months
	 * @return
	 */
	public static String addMonths(String date, int months) {
		return addMonths(date, months, DATE_DEFAULT_PATTERN);
	}
	
	/**
	 * 입력받은 일자에 대해서 해당 개월수만큼 더한 일자 반환한다. 
	 * <p>
	 * 마이너스 개월수는 입력받은 일자보다 이전 일자로 계산해서 반환한다.
	 * 
	 * @param date
	 * @param months
	 * @param pattern
	 * @return
	 */
	public static String addMonths(String date, int months, String pattern) {
		return addDate(date, 0, months, 0, pattern);
	}
	
	/**
	 * 입력받은 일자에 대해서 해당 년수만큼 더한 일자 반환한다. 
	 * <p>
	 * 마이너스 년수는 입력받은 일자보다 이전 일자로 계산해서 반환한다.
	 * 
	 * @param date
	 * @param years
	 * @return
	 */
	public static String addYears(String date, int years) {
		return addYears(date, years, DATE_DEFAULT_PATTERN);
	}
	
	/**
	 * 입력받은 일자에 대해서 해당 년수만큼 더한 일자 반환한다. 
	 * <p>
	 * 마이너스 년수는 입력받은 일자보다 이전 일자로 계산해서 반환한다.
	 * 
	 * @param date
	 * @param years
	 * @param pattern
	 * @return
	 */
	public static String addYears(String date, int years, String pattern) {
		return addDate(date, years, 0, 0, pattern);
	}
	
	/**
	 * 입력된 일자에 대해서 년,월,일만큼 가감해서 계산한 일자 반환한다.
	 * 
	 * @param date
	 * @param years
	 * @param months
	 * @param days
	 * @param pattern
	 * @return
	 */
	private static String addDate(String date, int years, int months, int days, String pattern) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern);
		DateTime dateTime = dateTimeFormatter.parseDateTime(date);
		
		if (years != 0) {
			dateTime = dateTime.withFieldAdded(DurationFieldType.years(), years);			
		}
		
		if (months != 0) {
			dateTime = dateTime.withFieldAdded(DurationFieldType.months(), months);
		}
		
		if (days != 0) {
			dateTime = dateTime.withFieldAdded(DurationFieldType.days(), days);
		}
		
		return dateTimeFormatter.print(dateTime);
	}	
	
	/**
	 * 입력받은 일자에 대해서 해당 시간만큼 더한 일자를 반환한다. 
	 * <p>
	 * 마이너스 시간은 입력받은 일자보다 이전의 일자로 계산해서 반환한다.
	 *
	 * @param date
	 * @param hours
	 * @return
	 */
	public static String addHours(String date, int hours) {
		return addHours(date, hours, DATE_HMS_PATTERN);
	}
	
	/**
	 * 입력받은 일자에 대해서 해당 시간만큼 더한 일자를 반환한다. 
	 * <p>
	 * 마이너스 시간은 입력받은 일자보다 이전의 일자로 계산해서 반환한다.
	 * 
	 * @param date
	 * @param hours
	 * @param pattern
	 * @return
	 */
	public static String addHours(String date, int hours, String pattern) {
		return addTime(date, 0, 0, 0, hours, 0, 0, pattern);
	}
	
	/**
	 * 입력받은 일자에 대해서 해당 분 만큼 더한 일자 반환한다. 
	 * <p>
	 * 마이너스 분은 입력받은 일자보다 이전 일자로 계산해서 반환한다.
	 * 
	 * @param date
	 * @param minutes
	 * @return
	 */
	public static String addMinutes(String date, int minutes) {
		return addMinutes(date, minutes, DATE_HMS_PATTERN);
	}
	
	/**
	 * 입력받은 일자에 대해서 해당 분 만큼 더한 일자 반환한다. 
	 * <p>
	 * 마이너스 분은 입력받은 일자보다 이전 일자로 계산해서 반환한다.
	 * 
	 * @param date
	 * @param minutes
	 * @param pattern
	 * @return
	 */
	public static String addMinutes(String date, int minutes, String pattern) {
		return addTime(date, 0, 0, 0, 0, minutes, 0, pattern);
	}
	
	/**
	 * 입력받은 일자에 대해서 해당 초만큼 더한 일자 반환한다. 
	 * <p>
	 * 마이너스 초는 입력받은 일자보다 이전 일자로 계산해서 반환한다.
	 * 
	 * @param date
	 * @param seconds
	 * @return
	 */
	public static String addSeconds(String date, int seconds) {
		return addSeconds(date, seconds, DATE_HMS_PATTERN);
	}
	
	/**
	 * 입력받은 일자에 대해서 해당 초만큼 더한 일자 반환한다. 
	 * <p>
	 * 마이너스 초는 입력받은 일자보다 이전 일자로 계산해서 반환한다.
	 * 
	 * @param date
	 * @param years
	 * @param pattern
	 * @return
	 */
	public static String addSeconds(String date, int seconds, String pattern) {
		return addTime(date, 0, 0, 0, 0, 0, seconds, pattern);
	}
	
	/**
	 * 입력된 일자에 대해서 년, 월, 일, 시, 분, 초 만큼 가감해서 계산한 일자 반환한다.
	 *
	 * @param date
	 * @param years
	 * @param months
	 * @param days
	 * @param hours
	 * @param minutes
	 * @param seconds
	 * @param pattern
	 * @return
	 */
	private static String addTime(String date, int years, int months, int days, int hours, int minutes, int seconds, String pattern) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern);
		DateTime dateTime = dateTimeFormatter.parseDateTime(date);
		
		if (years != 0) {
			dateTime = dateTime.withFieldAdded(DurationFieldType.years(), years);			
		}
		
		if (months != 0) {
			dateTime = dateTime.withFieldAdded(DurationFieldType.months(), months);
		}
		
		if (days != 0) {
			dateTime = dateTime.withFieldAdded(DurationFieldType.days(), days);
		}
		
		if (hours != 0) {
			dateTime = dateTime.withFieldAdded(DurationFieldType.hours(), hours);
		}		
		
		if (minutes != 0) {
			dateTime = dateTime.withFieldAdded(DurationFieldType.minutes(), minutes);
		}
		
		if (seconds != 0) {
			dateTime = dateTime.withFieldAdded(DurationFieldType.seconds(), seconds);
		}
		
		return dateTimeFormatter.print(dateTime);
	}
	
	/**
	 * 입력일 기준 이달 첫번째 일자 반환한다.
	 * 
	 * @param date
	 * @return
	 */
	public static String getStartDateOfMonth(String date) {
		return getStartDateOfMonth(date, DATE_DEFAULT_PATTERN);
	}
	
	/**
	 * 입력일 기준 이달 첫번째 일자 반환한다.
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getStartDateOfMonth(String date, String pattern) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern);
		
		DateTime startDate = dateTimeFormatter.parseDateTime(date);
		startDate = startDate.dayOfMonth().withMinimumValue();
		
		return dateTimeFormatter.print(startDate);
	}
	
	/**
	 * 입력일 기준 이달 마지막 일자 반환한다.
	 * 
	 * @param date
	 * @return
	 */
	public static String getLastDateOfMonth(String date) {
		return getLastDateOfMonth(date, DATE_DEFAULT_PATTERN);
	}
	
	/**
	 * 입력일 기준 이달 마지막 일자 반환한다.
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getLastDateOfMonth(String date, String pattern) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern);
		
		DateTime endDate = dateTimeFormatter.parseDateTime(date);
		endDate = endDate.dayOfMonth().withMaximumValue();
		
		return dateTimeFormatter.print(endDate);
	}
	
	/**
	 * 입력일 기준 이달 마지막 일자 반환한다.
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static int getLastDayOfMonth(String date, String pattern) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern);
		
		DateTime endDate = dateTimeFormatter.parseDateTime(date);
		
		return endDate.dayOfMonth().withMaximumValue().getDayOfMonth();
	}	
	
	/**
	 * 입력일 기준 해당년도의 주차를 반환한다.
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static int getWeekOfYear(String date, String pattern) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern);
		DateTime dateTime =  dateTimeFormatter.parseDateTime(date);
		
		return dateTime.getWeekOfWeekyear();
	}
	
	/**
	 * 해당 날짜의 주의 마지막 날을 반환한다.
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */	
	public static String getLastDayOfWeek(String date, String pattern) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern);
		DateTime dateTime =  dateTimeFormatter.parseDateTime(date);

		DateTime value = dateTime.dayOfWeek().withMaximumValue().minusDays(1);
		
		return dateTimeFormatter.print(value);
	}	
	
	/**
	 * 해당 날짜의 주의 시작날을 반환한다.
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */	
	public static String getStartDayOfWeek(String date, String pattern) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern);
		DateTime dateTime =  dateTimeFormatter.parseDateTime(date);

		DateTime value = dateTime.dayOfWeek().withMinimumValue().minusDays(1);
		
		return dateTimeFormatter.print(value);
	}	
	
	/**
	 * 해당 월이 몇주인가를 나타낸다
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */	
	public static int getTotalWeeksOfMonth(String date, String pattern) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern);
		DateTime dateTime =  dateTimeFormatter.parseDateTime(date);
		
		DateTime startDayOfMonth = dateTime.dayOfMonth().withMinimumValue();
		DateTime lastDayOfMonth = dateTime.dayOfMonth().withMaximumValue();

		Weeks weeksBetween = Weeks.weeksBetween(startDayOfMonth, lastDayOfMonth);

		return weeksBetween.plus(1).getWeeks();
	}
	
	/**
	 * 시작일자와 종료일자 사이의 일수를 반환한다
	 *
	 * @param startDate
	 * @param lastDate
	 * @param format
	 * @return
	 */
	public static int getTermDays(String startDate, String lastDate, String pattern) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern);
		DateTime startDateTime =  dateTimeFormatter.parseDateTime(startDate);
		DateTime lastDateTime =  dateTimeFormatter.parseDateTime(lastDate);
		
		Preconditions.checkArgument(compareStartAndEndDate(startDateTime, lastDateTime), "The start DateTime must be lower than the last one");

		return Days.daysBetween(startDateTime, lastDateTime).getDays();
	}

	/**
	 * <pre>
	 * 시작일자와 종료일자의 크기를 비교한다.
	 * </pre>
	 * <p>
	 * 종료일자는 반드시 시작일자보다 커야한다. 그렇지 않으면 예외를 발생시킨다.
	 *
	 * @param startDateTime
	 * @param lastDateTime
	 * @return
	 */
	public static boolean compareStartAndEndDate(DateTime startDateTime,
			DateTime lastDateTime) {
		return startDateTime.isBefore(lastDateTime);
	}
	
	/**
	 * 현재 timestamp 값 반환한다.
	 * 
	 * @return
	 */
	public static String getTimeStamp() {
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(TIMESTAMP_PATTERN);
		DateTime dateTime = new DateTime();
		
		return dateTimeFormatter.print(dateTime);
	}
	
	/**
	 * 패턴에 맞게 들어온 문자열을 java.sql.Date 타입으로 변환한다.
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static java.sql.Date stringToSqlDate(String date, String pattern) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern);
		
		return new java.sql.Date(dateTimeFormatter.parseDateTime(date).getMillis());
	}
		
	/**
	 * 패턴에 맞게 들어온 문자열을 java.sql.Timestamp 타입으로 변환한다.
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static Timestamp stringToTimestamp(String date, String pattern) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern);
		
		return new Timestamp(dateTimeFormatter.parseDateTime(date).getMillis());
	}
		
	/**
	 * java.sql.Timestamp 타입의 일자를 패턴에 맞는 문자열로 변환한다.
	 *
	 * @param date
	 * @param format
	 */
	public static String timestampToString(Timestamp timestamp, String pattern) {
		Assert.notNull(timestamp, "timestamp must be set");
		
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern);
		return dateTimeFormatter.print(timestamp.getTime());
	}

	/**
	 * source문자열을 targetPattern형태의 문자열로 변환한다.
	 *
	 * @param source 특정날짜문자열
	 * @param sourcePattern source문자열에 대한 포맷. DateUtil클래스의 static field참고 
	 * @param targetPattern 변환할 문자열에 대한 포맷. DateUtil클래스의 static field참고
	 * 
	 * @return targetPattern형태로 변환된 문자열
	 */
	public static String toConvertedString(String source, String sourcePattern, String targetPattern) {
		DateTime sourceDateTime = DateTimeFormat.forPattern(sourcePattern).parseDateTime(source);
		
		return sourceDateTime.toString(DateTimeFormat.forPattern(targetPattern));
	}

	/**
	 * checkDateTimeFormat
	 *
	 * @param source 특정날짜문자열
	 * @param format source문자열에 대한 포맷. DateUtil클래스의 static field참고
	 * @return
	 */
	public static boolean checkDateTimeFormat(String source, String format) {
		boolean isDateTimeFormat = true;
		
		try {
			DateTimeFormat.forPattern(format).parseDateTime(source);
		} catch (IllegalArgumentException e) {
			isDateTimeFormat = false;
		}
		
		return isDateTimeFormat;
	}
	
	
	/**
	 * getLongToString
	 *
	 * @param milliSeconds long 타입 밀리세컨
	 * @return 시:분:초
	 */
	public static String getLongToString( long milliSeconds ) {

	    long time       = milliSeconds / 1000;
	    String seconds = Integer.toString( ( int )( time % 60 ) );
	    String minutes = Integer.toString( ( int )( ( time % 3600 ) / 60 ) );
	    String hours   = Integer.toString( ( int )( time / 3600 ) );

	    for( int i = 0; i < 2; i++ ) {
	      if( seconds.length() < 2 ) {
	        seconds = "0" + seconds;
	      }
	      if( minutes.length() < 2 ) {
	        minutes = "0" + minutes;
	      }
	      if( hours.length() < 2 ) {
	        hours = "0" + hours;
	      }
	    }

	    String ddate = hours + ":" + minutes + ":" + seconds;
	    return ddate;
	}
	
	/**
	 * getSecToString
	 *
	 * @param milliSeconds long 타입 밀리세컨
	 * @return 시:분:초
	 */
	public static String getSecToString( int time ) {
		
		String seconds = Integer.toString( ( int )( time % 60 ) );
	    String minutes = Integer.toString( ( int )( ( time % 3600 ) / 60 ) );
	    String hours   = Integer.toString( ( int )( time / 3600 ) );

	    for( int i = 0; i < 2; i++ ) {
	      if( seconds.length() < 2 ) {
	        seconds = "0" + seconds;
	      }
	      if( minutes.length() < 2 ) {
	        minutes = "0" + minutes;
	      }
	      if( hours.length() < 2 ) {
	        hours = "0" + hours;
	      }
	    }

	    String ddate = hours + ":" + minutes + ":" + seconds;
	    return ddate;
	}
}