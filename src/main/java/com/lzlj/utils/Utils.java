package com.lzlj.utils;

import java.io.Closeable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class Utils {
	public static final int SECOND = 1000;
	public static final int MINUTE = SECOND * 60;
	public static final int HOUR = MINUTE * 60;
	public static final int DAY = HOUR * 24;
	
	public static final String YYYYMMDD = "yyyyMMdd";
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String YYYYMMDDHH = "yyyyMMddHH";
	public static final String YYYYMMDDHHMM = "yyyyMMddHHmm";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String YYYY_MM_DD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static final String HH_MM_SS=  "HH:mm:ss";
	
	public static <T extends Object> boolean isNull(T t) {
		return t == null;
	}
	
	public static <T extends Object> boolean isNotNull(T t) {
		return t != null;
	}
	
	public static boolean isBlank(String str) {
		return str == null || str.trim().length() <= 0;
	}
	
	public static boolean isNotBlank(String str) {
		return str != null && str.trim().length() > 0;
	}
	
	
	public static String ignoreFirstTrim(String str, int ignoreCount) {
		if (isNull(str)) {
			return str;
		}
		return ignoreFirst(str.trim(), ignoreCount);
	}
	
	public static String ignoreFirst(String str, int ignoreCount) {
		if (isNull(str)) {
			return str;
		}
		int len = str.length();
		if (ignoreCount >= len) {
			return "";
		}
		if (ignoreCount <= 0) {
			return str;
		}
		return str.substring(ignoreCount);
	}
	
	public static String ignoreLastTrim(String str, int ignoreCount) {
		if (isNull(str)) {
			return str;
		}
		return ignoreLast(str.trim(), ignoreCount);
	}
	
	public static String ignoreLast(String str, int ignoreCount) {
		if (isNull(str)) {
			return str;
		}
		int len = str.length();
		if (ignoreCount >= len) {
			return "";
		}
		if (ignoreCount <= 0) {
			return str;
		}
		return str.substring(0, len - ignoreCount);
	}
	
	
	public static String toSafeString(String str) {
		return str == null ? "" : str;
	}
	
	public static <T extends Object> boolean isEmpty(T[] ts) {
		return ts == null || ts.length <= 0;
	}
	
	public static <T extends Object> boolean isNotEmpty(T[] ts) {
		return ts != null && ts.length > 0;
	}
	
	public static <T extends Object> boolean isEmpty(Collection<T> ts) {
		return ts == null || ts.size() <= 0;
	}
	
	public static <T extends Object> boolean isNotEmpty(Collection<T> ts) {
		return ts != null && ts.size() > 0;
	}
	
	public static <T extends Object> boolean isEmpty(Map<T, T> map) {
		return map == null || map.size() <= 0;
	}
	
	public static <K extends Object, V extends Object> boolean isNotEmpty(Map<K, V> map) {
		return map != null && map.size() > 0;
	}
	
	public static Double toDouble(String dStr) {
		try {
			return Double.parseDouble(dStr.trim());
		} catch (Exception e) {
			// ignore
		}
		return null;
	}
	
	public static Integer toInt(String iStr) {
		try {
			return Integer.parseInt(iStr.trim());
		} catch (Exception e) {
			// ignore
		}
		return null;
	}
	
	public static Long toLong(String lStr) {
		try {
			return Long.parseLong(lStr.trim());
		} catch (Exception e) {
			// ignore
		}
		return null;
	}
	
	public static Integer randomInt(int factor) {
		return (int)(Math.random() * factor);
	}
	
	public static Double randomDouble(int factor) {
		return Math.random() * factor;
	}
	
	public static Date parseDate(String dateStr, String datePattern) {
		if (Utils.isBlank(dateStr) || Utils.isBlank(datePattern)) {
			return null;
		}
		try {
			return new SimpleDateFormat(datePattern).parse(dateStr);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String formatDate_YYYYMMDDHHMM(Date date) {
		if (Utils.isNull(date)) {
			return "";
		}
		return new SimpleDateFormat(YYYYMMDDHHMM).format(date);
	}
        
        public static String formatDate_YYYYMMDDHHMMSS(Date date) {
		if (Utils.isNull(date)) {
			return "";
		}
		return new SimpleDateFormat(YYYYMMDDHHMMSS).format(date);
	}
        
    public static String formatDate_HH_MM_SS(Date date) {
		if (Utils.isNull(date)) {
			return "";
		}
		return new SimpleDateFormat(HH_MM_SS).format(date);
	}
	
	public static String nowDay_YYYYMMDD() {
		return new SimpleDateFormat(YYYYMMDD).format(new Date());
	}
	
	public static String addDay_YYYYMMDD(int beforeDay) {
		return new SimpleDateFormat(YYYYMMDD).format(addDay(beforeDay));
	}
	
	public static String addDay_YYYY_MM_DD(int beforeDay) {
		return new SimpleDateFormat(YYYY_MM_DD).format(addDay(beforeDay));
	}
	
	public static Date addDay(int day) {
		SimpleDateFormat df = new SimpleDateFormat(YYYYMMDD);
		try {
			Date nowDay = df.parse(df.format(new Date()));
			Calendar c = Calendar.getInstance();
			c.setTime(nowDay);
			c.add(Calendar.DAY_OF_MONTH, day);
			return c.getTime();
		} catch (ParseException e) {
			// ignore
		}
		return null;
	}
	
	public static Date addHour(int hour) {
		SimpleDateFormat df = new SimpleDateFormat(YYYYMMDDHH);
		try {
			Date nowDay = df.parse(df.format(new Date()));
			Calendar c = Calendar.getInstance();
			c.setTime(nowDay);
			c.add(Calendar.HOUR_OF_DAY, hour);
			return c.getTime();
		} catch (ParseException e) {
			// ignore
		}
		return null;
	}
	
	public static String addHour_yyyyMMddHH(int hour) {
		SimpleDateFormat df = new SimpleDateFormat(YYYYMMDDHH);
		return df.format(addHour(hour));
	}
	
	public static <T extends Object> T random(T[] ts) {
		if (isEmpty(ts)) {
			return null;
		}
		return ts[Math.abs(UUID.randomUUID().toString().hashCode()) % ts.length];
	}
	
	public static String formatMillseconds(long millseconds) {
		StringBuffer sb = new StringBuffer(12);
		long day = millseconds / DAY;
		if (day > 0) {
			sb.append(day).append('天');
			millseconds = millseconds % DAY;
		}
		
		long hour = millseconds / HOUR;
		if (hour > 0) {
			sb.append(hour).append("小时");
			millseconds = millseconds % HOUR;
		}
		
		long minute = millseconds / MINUTE;
		if (minute > 0) {
			sb.append(minute).append("分");
			millseconds = millseconds % MINUTE;
		}
		
		long second = millseconds / SECOND;
		if (second > 0) {
			sb.append(second).append('秒');
			millseconds = millseconds % SECOND;
		}
		
		sb.append(millseconds).append("毫秒");
		return sb.toString();
	}
	
	
	public static <T extends Closeable> void closeQuietly(T t) {
		try {
			if (isNotNull(t)) {
				t.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void delayMillseconds(long millseconds) {
		try {
			Thread.sleep(millseconds);
		} catch (Exception e) {
			// ignore
		}
	}
	
	public static List<Integer[]> pagination(int pageSize, int totalCount) {
		List<Integer[]> rtnList = new ArrayList<Integer[]>();
		int pageCount = (totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize + 1));
		Integer[] pages;
		int k = 1, diff;
		for (int i = 0; i < pageCount; i++) {
			diff = totalCount - k + 1;
			if (diff < pageSize) {
				pages = new Integer[diff];
				for (int j = 0; j < diff; j++) {
					pages[j] = k++;
				}
			} else {
				pages = new Integer[pageSize];
				for (int j = 0; j < pageSize; j++) {
					pages[j] = k++;
				}
			}
			rtnList.add(pages);
		}
		return rtnList;
	}
	
	public static int calcPageCount(int pageSize, int totalCount) {
		if (totalCount <= 0) {
			return 0;
		}
		return (totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize + 1));
	}
	
	/**
	 * 将类名或者属性名转换为数据库表或者字段名，下划线分隔单词
	 * @param str
	 * @return
	 */
	public static String toDBName(String str) {
		if (Utils.isBlank(str)) {
			return "";
		}
		char[] cs = str.toCharArray();
		int len = cs.length;
		String rst = "";
		for (int i = 0; i < len; i++) {
			if (isNotBlank(rst) && Character.isUpperCase(cs[i])) {
				rst += '_';
			}
			rst += cs[i];
		}
		return rst.toLowerCase();
	}
	
	public static long random(long max) {
		return (long) (Math.random() * max);
	}
	
	public static String encode(String str, String encoding) {
		String encode = str;
		try {
			encode = URLEncoder.encode(str, encoding);
		} catch (UnsupportedEncodingException e) {
			// ignore
		}
		return encode;
	}
	
	public static String trim(String str) {
		return str == null ? "" : str.trim();
	}
	
	public static <T extends Object> String join(Collection<T> objs, String separater) {
		if (isEmpty(objs)) {
			return "";
		}
		String rst = "";
		int i = 0;
		for (Object obj : objs) {
			if (i > 0) {
				rst += separater;
			}
			rst += obj;
			i++;
		}
		return rst;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Object> T instance(String classFullName) {
		try {
			return (T)Class.forName(classFullName).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String args[]) {
		Set<String> s = new HashSet<String>();
		for (int i = 0; i < 24; i++) {
			s.add(i + "");
		}
		System.out.println(join(s, ","));
	}
}
