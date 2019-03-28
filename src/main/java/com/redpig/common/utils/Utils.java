package com.redpig.common.utils;

import com.redpig.common.consts.Consts;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用工具类
 */
@SuppressWarnings("restriction")
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
	
	public static Long substract(Long l1, Long l2) {
		if (isNull(l1) && isNotNull(l2)) {
			return -l2;
		} else if (isNull(l1) && isNull(l2)) {
			return 0L;
		} else if (isNotNull(l1) && isNotNull(l2)) {
			return l1 - l2;
		} else {
			return l1;
		}
	}
	
	public static BigDecimal substract(BigDecimal b1, BigDecimal b2) {
		if (isNull(b1) && isNotNull(b2)) {
			return new BigDecimal("0").subtract(b2);
		} else if (isNull(b1) && isNull(b2)) {
			return new BigDecimal("0");
		} else if (isNotNull(b1) && isNotNull(b2)) {
			return b1.subtract(b2);
		} else {
			return b1;
		}
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
			return Double.parseDouble(dStr);
		} catch (Exception e) {
			// ignore
		}
		return null;
	}
	
	public static Integer toInt(String iStr) {
		try {
			return Integer.parseInt(iStr);
		} catch (Exception e) {
			// ignore
		}
		return null;
	}
	
	public static Long toLong(String lStr) {
		try {
			return Long.parseLong(lStr);
		} catch (Exception e) {
			// ignore
		}
		return null;
	}
	
	public static BigDecimal toBigDecimal(String s) {
		try {
			return new BigDecimal(s);
		} catch (Exception e) {
			// ignore
		}
		return null;
	}
	
	public static Long[] toLongs(String lStr, String separator) {
		if (Utils.isBlank(lStr)) {
			return null;
		}
		String[] ls = StringUtils.split(lStr, separator);
		List<Long> longs = new ArrayList<Long>();
		Long lv;
		for (String l : ls) {
			lv = toLong(l);
			if (isNotNull(lv)) {
				longs.add(lv);
			}
		}
		return longs.toArray(new Long[0]);
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

	public static String formatDate(Date date, String format) {
		if (Utils.isNull(date) || Utils.isBlank(format)) {
			return "";
		}
		return new SimpleDateFormat(format).format(date);
	}
	
	public static String formatDateStr(String dateStr, String oldFormat, String newFormat){
		return Utils.formatDate(Utils.parseDate(dateStr, oldFormat), newFormat);
	}
	
	public static String formatDate_YYYYMMDDHH(Date date) {
		if (Utils.isNull(date)) {
			return "";
		}
		return new SimpleDateFormat(YYYYMMDDHH).format(date);
	}
        
    public static String formatDate_YYYYMMDDHHMMSS(Date date) {
		if (Utils.isNull(date)) {
			return "";
		}
		return new SimpleDateFormat(YYYYMMDDHHMMSS).format(date);
	}
    
    public static String formatDate_YYYY_MM_DD(Long timeMillis){
    	return new SimpleDateFormat(YYYY_MM_DD).format(new Date(timeMillis));
    }
    
    public static String formatDate_YYYY_MM_DD(Date date){
    	if (Utils.isNull(date)) {
			return "";
		}
    	return new SimpleDateFormat(YYYY_MM_DD).format(date);
    }
    
    public static String formatDate_YYYY_MM_DD_HHMMSS(Long timeMillis){
    	return new SimpleDateFormat(YYYY_MM_DD_HHMMSS).format(new Date(timeMillis));
    }
    
    public static String formatDate_YYYY_MM_DD_HHMMSS(Date date){
    	if (Utils.isNull(date)) {
			return "";
		}
    	return new SimpleDateFormat(YYYY_MM_DD_HHMMSS).format(date);
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
	
	public static String formatTime(long millseconds) {
		StringBuffer sb = new StringBuffer();
		long day = millseconds / Consts.DAY;
		if (day > 0) {
			sb.append(day).append('天');
			millseconds = millseconds % Consts.DAY;
		}
		
		long hour = millseconds / Consts.HOUR;
		if (hour > 0) {
			sb.append(hour).append("小时");
			millseconds = millseconds % Consts.HOUR;
		}
		
		long minute = millseconds / Consts.MINUTE;
		if (minute > 0) {
			sb.append(minute).append("分");
			millseconds = millseconds % Consts.MINUTE;
		}
		
		long second = millseconds / Consts.SECOND;
		if (second > 0) {
			sb.append(second).append('秒');
			millseconds = millseconds % Consts.SECOND;
		}
		
		sb.append(millseconds).append("毫秒");
		
		return sb.toString();
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
	
	/**
	 * 根据字符串获取时间戳
	 * @param time_str 要判断的时间字符串（格式如3小时前，3天前，3周前，昨天,2016年11月21日，2016年11月21日 13:11）
	 * @return 13位时间戳 （毫秒）
	 */
	public static long getTimeFromStr(String time_str){
		long time = System.currentTimeMillis();
		SimpleDateFormat format = null;
		if(time_str.contains("分钟前")){
			int mins = Integer.parseInt(time_str.trim().substring(0, time_str.indexOf("分钟前")));
			time = time - mins * 60 * 1000;
		}else if(time_str.contains("小时前")){
			int hours = Integer.parseInt(time_str.trim().substring(0, time_str.indexOf("小时前")));
			time = time - hours * 3600 * 1000;
		}else if(time_str.contains("昨天")){
			time = time - 86400 * 1000;
		}else if(time_str.contains("天前")){
			int days = Integer.parseInt(time_str.trim().substring(0, time_str.indexOf("天前")));
			time = time - days * 86400 * 1000;
		}else if(time_str.contains("周前")){
			int weeks = Integer.parseInt(time_str.trim().substring(0, time_str.indexOf("周前")));
			time = time - weeks * 604800 * 1000;
		}else if(time_str.contains("月") && !time_str.contains("年")){
			format = new SimpleDateFormat("yyyy");
	        Date date = new Date();
	        String year = format.format(date);
	        format = new SimpleDateFormat("yyyy年MM月dd日");
			try {
				 time = format.parse(year+"年"+time_str).getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			 
		}else if (time_str.contains("月") && time_str.contains("年") && !time_str.contains(":")) {
			format = new SimpleDateFormat("yyyy年MM月dd日");
			try {
				time = format.parse(time_str).getTime();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if (time_str.contains("月") && time_str.contains("年") && time_str.contains(":")){
			String[] tmp_strs = time_str.split(" ");
			if(tmp_strs != null && tmp_strs.length == 2){
				String timeStr = tmp_strs[1];
				if (timeStr.length() > 5){
					format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
				}else{
					format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
				}
			}
			if(format != null){
				try {
					time = format.parse(time_str).getTime();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		return time;
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
	
	public static String toGetterMethodName(String fieldName, Class<?> type) {
		if (isBlank(fieldName)) {
			return "";
		}
		boolean isBooleanType = boolean.class.equals(type);
		fieldName = fieldName.trim();
		int len = fieldName.length();
		return (isBooleanType ? "is" : "get") + Character.toUpperCase(fieldName.charAt(0)) + (len > 1 ? fieldName.substring(1) : "");
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
	
	public static String extractParamValue(String link, String paramKey) {
		if (isBlank(link) || isBlank(paramKey)) {
			return "";
		}
		String[] ps = StringUtils.split(link, "?");
		for (String p : ps) {
			if (Utils.isBlank(p) || !p.contains("=")) {
				continue;
			}
			String[] parts = StringUtils.split(p, "&"), tmp;
			for (String part : parts) {
				if (isNotBlank(part) && part.contains("=")) {
					tmp = StringUtils.split(part, "=");
					if (isNotEmpty(tmp) && tmp.length == 2 && trim(tmp[0]).equals(paramKey)) {
						return tmp[1];
					}
				}
			}
		}
		return "";
	}
	
	public static Integer extractInteger(String s) {
		if (isBlank(s)) {
			return null;
		}
		char[] cs = s.toCharArray();
		String si = "";
		for (char c : cs) {
			if (isNotBlank(si) && !Character.isDigit(c)) {
				break;
			}
			if (Character.isDigit(c)) {
				si += c;
			}
		}
		return toInt(si);
	}
	
	public static Integer[] extractIntegers(String s) {
		if (isBlank(s)) {
			return null;
		}
		char[] cs = s.toCharArray();
		String si = "";
		List<Integer> rst = new ArrayList<Integer>();
		char c;
		int len = cs.length;
		for (int i = 0; i < len; i++) {
			c = cs[i];
			if (Character.isDigit(c)) {
				si += c;
			} else {
				if (isNotBlank(si)) {
					rst.add(toInt(si));
					si = "";
				}
			}
			if (i == len - 1 && isNotBlank(si)) {
				rst.add(toInt(si));
			}
		}
		return rst.toArray(new Integer[0]);
	}
	
	public static Long extractLong(String s) {
		if (isBlank(s)) {
			return null;
		}
		char[] cs = s.toCharArray();
		String si = "";
		for (char c : cs) {
			if (isNotBlank(si) && !Character.isDigit(c)) {
				break;
			}
			if (Character.isDigit(c)) {
				si += c;
			}
		}
		return toLong(si);
	}
	
	public static String extractNumber(String s) {
		if (isBlank(s)) {
			return null;
		}
		char[] cs = s.toCharArray();
		String si = "";
		for (char c : cs) {
			if (isNotBlank(si) && !Character.isDigit(c)) {
				break;
			}
			if (Character.isDigit(c)) {
				si += c;
			}
		}
		return si;
	}
	
	public static Double extractDouble(String s) {
		if (isBlank(s)) {
			return null;
		}
		char[] cs = s.toCharArray();
		String sd = "";
		for (char c : cs) {
			if (isNotBlank(sd) && !Character.isDigit(c) && c != '.') {
				break;
			}
			if (isNotBlank(sd) && sd.contains(".") && !Character.isDigit(c)) {
				break;
			}
			if (Character.isDigit(c) || c == '.') {
				sd += c;
			}
		}
		return toDouble(sd);
	}
	
	public static BigDecimal extractBigDecimal(String s) {
		if (isBlank(s)) {
			return null;
		}
		char[] cs = s.toCharArray();
		String sd = "";
		for (char c : cs) {
			if (isNotBlank(sd) && !Character.isDigit(c) && c != '.') {
				break;
			}
			if (isNotBlank(sd) && sd.contains(".") && !Character.isDigit(c)) {
				break;
			}
			if (Character.isDigit(c) || c == '.') {
				sd += c;
			}
		}
		return toBigDecimal(sd);
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
	
	public static <T extends Object> String join(T[] objs, String separater) {
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void collectPropNamesByAnnotation(List<String> props, Class<?> clazz, Class annotationClass) throws Exception {
		Field[] fields = clazz.getDeclaredFields();
		
		if (isNotEmpty(fields)) {
			Annotation annotation;
			for (Field field : fields) {
				annotation = field.getAnnotation(annotationClass);
				if (annotation != null) {
					props.add(String.valueOf(field.getName()));
				}
			}
		}
		
		Class<?> superclass = clazz.getSuperclass();
		while (Utils.isNotNull(superclass)) {
			collectPropNamesByAnnotation(props, superclass, annotationClass);
			superclass = superclass.getSuperclass();
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Annotation getMethodOrClassAnnotation(Method method, Class annotationClass) throws Exception {
		Annotation annotation = method.getAnnotation(annotationClass);
		if (Utils.isNull(annotation)) {
			Class<?> clazz = method.getDeclaringClass();
			annotation = clazz.getAnnotation(annotationClass);
			if (Utils.isNull(annotation)) {
				Class superclass = clazz.getSuperclass();
				while (Utils.isNull(annotation) && Utils.isNotNull(superclass)) {
					annotation = superclass.getAnnotation(annotationClass);
					superclass = superclass.getSuperclass();
				}
			}
		}
		return annotation;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Annotation getMethodAnnotation(Method method, Class annotationClass) throws Exception {
		return method.getAnnotation(annotationClass);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Annotation getClassAnnotation(Class clazz, Class annotationClass) throws Exception {
		Annotation annotation = clazz.getAnnotation(annotationClass);
		if (Utils.isNull(annotation)) {
			Class superclass = clazz.getSuperclass();
			while (Utils.isNull(annotation) && Utils.isNotNull(superclass)) {
				annotation = superclass.getAnnotation(annotationClass);
				superclass = superclass.getSuperclass();
			}
		}
		return annotation;
	}
	
	@SuppressWarnings("rawtypes")
	public static List<String> getPropNamesByAnnotation(Class<?> clazz, Class annotationClass) throws Exception {
		List<String> props = new ArrayList<String>();
		collectPropNamesByAnnotation(props, clazz, annotationClass);
		return props;
	}
	
	public static String decode(String s, String encoding) {
		if (isBlank(s) || isBlank(encoding)) {
			return s;
		}
		try {
			return URLDecoder.decode(s, encoding);
		} catch (Exception e) {
			return s;
		}
	}
	
	public static String encrypt(String s) {
		if (isBlank(s)) {
			return s;
		}
		char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
        try {
            byte[] btInput = s.getBytes("UTF-8");
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return s;
        }
	}
	
	public static List<Field> getFields(Class<?> clazz) {
		List<Field> fields = new ArrayList<Field>();
		if (isNull(clazz)) {
			return fields;
		}
		Field[] fs = clazz.getDeclaredFields();
		if (isNotEmpty(fs)) {
			for (Field f : fs) {
				fields.add(f);
			}
		}
		Class<?> parent = clazz.getSuperclass();
		while (isNotNull(parent) && !parent.equals(Object.class)) {
			fs = parent.getDeclaredFields();
			if (isNotEmpty(fs)) {
				for (Field f : fs) {
					fields.add(f);
				}
			}
			parent = parent.getSuperclass();
		}
		return fields;
	}
	
	public static Method getMethod(Class<?> clazz, String methodName, Class<?>... types) {
		Method method = null;
		if (isNull(clazz) || isBlank(methodName)) {
			return method;
		}
		try {
			method = clazz.getDeclaredMethod(methodName, types);
		} catch (Exception e) {
			// ignored
		}
		Class<?> parent = clazz.getSuperclass();
		while (isNull(method) && isNotNull(parent) && !parent.equals(Object.class)) {
			try {
				method = parent.getDeclaredMethod(methodName, types);
			} catch (Exception e) {
				// ignored
			}
			parent = parent.getSuperclass();
		}
		return method;
	}
	
	public static boolean isSubClassOf(Class<?> clazz, Class<?> parentClass) {
		Class<?> parent = clazz.getSuperclass();
		while (isNotNull(parent)) {
			if (parent.equals(parentClass)) {
				return true;
			}
			parent = parent.getSuperclass();
		}
		return false;
	}
	
	public static List<String> readFile(File fileToRead, String encoding) throws Exception {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new BufferedInputStream(new FileInputStream(fileToRead), 512), Utils.isBlank(encoding) ? "utf-8" : encoding));
			List<String> lines = new ArrayList<String>();
			String line;
			while (!Utils.isNull(line = br.readLine())) {
				lines.add(line);
			}
			return lines;
		} finally {
			Utils.closeQuietly(br);
		}
	}
	
	public static String resplitBySpace(String s) {
		if (isBlank(s)) {
			return "";
		}
		String[] parts = StringUtils.split(s, " ");
		String rst = "";
		for (String part : parts) {
			if (isBlank(part)) {
				continue;
			}
			if (isNotBlank(rst)) {
				rst += " ";
			}
			rst += part;
		}
		return rst;
	}
	
	public static <T extends Object> void copyNonNullProperties(T source, T target) throws Exception {
		if (Utils.isNull(source) || Utils.isNull(target)) {
			return;
		}
		Class<?> clazz = source.getClass();
		BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		Method rMethod, wMethod;
		Object o = null;
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			rMethod = propertyDescriptor.getReadMethod();
			if (Utils.isNull(rMethod)) {
				continue;
			}
			o = rMethod.invoke(source);
			if (Utils.isNotNull(o)) {
				wMethod = propertyDescriptor.getWriteMethod();
				if (Utils.isNotNull(wMethod)) {
					wMethod.invoke(target, o);
				}
			}
		}
	}
	
	/**
	 * 用base64加密字符串
	 * @param str
	 * @return
	 */
	public static String base64EncodeStr(String str){
		if (Utils.isNotBlank(str)){
			BASE64Encoder encoder = new BASE64Encoder();
			return encoder.encode(str.getBytes());
		}
		return null;
	}
	
	/**
	 * 用base64解密字符串
	 * @param str
	 * @return
	 */
	public static String base64DecodeStr(String str){
		if (Utils.isNotBlank(str)){
			try{
				BASE64Decoder decoder = new BASE64Decoder();
				return new String(decoder.decodeBuffer(str), "UTF-8");
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 将图片转换为base64字符
	 * @param path
	 * @return
	 */
	public static String imgToBase64(String path){
		if (Utils.isNotBlank(path)){
			InputStream in = null;  
	        byte[] data = null;  
	        try{  
	            in = new FileInputStream(path);          
	            data = new byte[in.available()];  
	            in.read(data);  
	            in.close();  
	        } catch (IOException e){  
	            e.printStackTrace();  
	        }
	        if(data != null){
	        	BASE64Encoder encoder = new BASE64Encoder();  
	 	        return encoder.encode(data);
	        }
		}
        return null;
	}
	
	/**
	 * 将base64字符转换为图片
	 * @param base64Str
	 * @param imgPath
	 * @return
	 */
	public static Boolean Base64ToImg(String base64Str, String imgPath){
		if(Utils.isNotBlank(base64Str) && Utils.isNotBlank(imgPath)){
			try{
				BASE64Decoder decoder = new BASE64Decoder();
				byte[] bytes = decoder.decodeBuffer(base64Str);
				for (int i = 0; i < bytes.length; ++i) {  
					if (bytes[i] < 0) {
						bytes[i] += 256;  
					}  
				}  
				OutputStream out = new FileOutputStream(imgPath);
				out.write(bytes);
				out.flush();
				out.close();
				return true;
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * 从文件读取文本
	 * @param filePath
	 * @return
	 */
	public static String getTextFromFile(String filePath){
		try {
			File file = new File(filePath);
			if(file.exists() && file.isFile()){
				FileInputStream is = new FileInputStream(file);
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				StringBuilder resultBuilder = new StringBuilder();
				String temp = null;
				while((temp = reader.readLine()) != null){
					resultBuilder.append(temp);
				}
				reader.close();
				is.close();
				return resultBuilder.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 从文件读取文本
	 * @param file 
	 * @return
	 */
	public static String getTextFromFile(File file){
		try {
			if(file.exists() && file.isFile()){
				FileInputStream is = new FileInputStream(file);
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				StringBuilder resultBuilder = new StringBuilder();
				String temp = null;
				while((temp = reader.readLine()) != null){
					resultBuilder.append(temp);
				}
				reader.close();
				is.close();
				return resultBuilder.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 用MD5加密字符串
	 * @param string
	 * @return
	 */
	public static String md5(String string) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("md5");
			md.update(string.getBytes("UTF-8"));
			byte[] md5Bytes = md.digest();
			return bytes2Hex(md5Bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String bytes2Hex(byte[] byteArray) {
		StringBuffer strBuf = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (byteArray[i] >= 0 && byteArray[i] < 16) {
				strBuf.append("0");
			}
			strBuf.append(Integer.toHexString(byteArray[i] & 0xFF));
		}
		return strBuf.toString();
	}
	
	/**
	 * 通过标题、时间和发布者名获取id
	 * @param content 标题
	 * @return 返回id
	 */
	public static String getEncryptCode(String content) {
		return md5(content);
	}
	
	/** 
     * @param md5L16 
     * @Description: 将16位的md5转化为long值 
     */  
    public static long parseMd5L16ToLong(String md5L16){  
    	long re = 0L;  
    	try{
    		if (md5L16 == null) {  
                throw new NumberFormatException("null");  
            }  
            md5L16 = md5L16.toLowerCase(); 
            byte[] bA = md5L16.getBytes("UTF-8");  
            for (int i = 0; i < bA.length; i++) {  
                //加下一位的字符时，先将前面字符计算的结果左移4位  
                re <<= 4;  
                //0-9数组  
                byte b = (byte) (bA[i] - 48);  
                //A-F字母  
                if (b > 9) {  
                    b = (byte) (b - 39);  
                }  
                //非16进制的字符  
                if (b > 15 || b < 0) {  
                    throw new NumberFormatException("For input string '" + md5L16);  
                }  
                re += b;  
            }  
    	}catch(Exception e){
    		e.printStackTrace();
    	}
        return re;  
    }  
    
    /**
     * 将字符串转化为16位Long数字 
     * @param content
     * @return
     */
    public static Long encryptStrToLong(String content){
    	try {
    		return parseMd5L16ToLong(md5(content));
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
	
    /**
     * 清除字符串中的某指定字符串
     * @param str
     * @param clearStr
     */
    public static String clear(String str, String clearStr){
    	if(Utils.isNotBlank(str) && Utils.isNotBlank(clearStr)){
    		return str.replace(clearStr, "");
    	}
    	return null;
    }
    
    /**
     * 清除字符串中的所有指定字符串
     * @param str
     * @param clearStr
     */
    public static String clearAll(String str, String clearStr){
    	if(Utils.isNotBlank(str) && Utils.isNotBlank(clearStr)){
    		return str.replaceAll(clearStr, "");
    	}
    	return null;
    }
    
    /**
	 * 解析出url请求的路径,即不包含参数部分
	 * @param strURL url地址
	 * @return url路径
	 */
	public static String getUrlPrefix(String strURL) {
		String strPage = null;
		if(Utils.isNotBlank(strURL)){
			String[] arrSplit = null;
			arrSplit = strURL.trim().toLowerCase().split("[?]");
			if (strURL.length() > 0) {
				if (arrSplit.length > 1) {
					if (arrSplit[0] != null) {
						strPage = arrSplit[0];
					}
				}
				else{
					strPage = strURL;
				}
			}
		}
		return strPage;
	}
	
	/**
	 * 过滤四个byte的表情符
	 * @param content 博文或评论内容
	 * @return
	 */
	public static String removeFourChar(String content) {
		try {
			byte[] conbyte = content.getBytes();
			for (int i = 0; i < conbyte.length; i++) {
				if ((conbyte[i] & 0xF8) == 0xF0) {
					for (int j = 0; j < 4; j++) {
						conbyte[i + j] = 0x30;
					}
					i += 3;
				}
			}
			content = new String(conbyte);
			content.replaceAll("0000", "");
	        Pattern p = Pattern.compile("['\\\\]");     
	        Matcher m = p.matcher(content);   
	        if(m.find()) {
	        	content = m.replaceAll("");
	        }
		} catch (Exception e) {
		}
		return content;
	}
	
	/**
	 * 去掉url中的路径，留下请求参数部分
	 * @param strURL url地址
	 * @return url请求参数部分
	 */
	private static String cutUrlParamsStr(String strURL) {
		String strAllParam = null;
		String[] arrSplit = null;
		strURL = strURL.trim().toLowerCase();
		arrSplit = strURL.split("[?]");
		if (strURL.length() > 1) {
			if (arrSplit.length > 1) {
				if (arrSplit[1] != null) {
					strAllParam = arrSplit[1];
				}
			}
		}
		return strAllParam;
	}
	
	/**
	 * 解析出url参数中的键值对存入map
	 * @param URL url地址
	 * @return url请求参数部分
	 */
	public static Map<String, String> getParamsFromUrl(String URL) {
		Map<String, String> mapRequest = new HashMap<String, String>();
		try{
			String[] arrSplit = null;
			String strUrlParam = cutUrlParamsStr(URL);
			if (strUrlParam == null) {
				return mapRequest;
			}
			arrSplit = strUrlParam.split("[&]");
			for (String strSplit : arrSplit) {
				String[] arrSplitEqual = null;
				arrSplitEqual = strSplit.split("[=]");
				if (arrSplitEqual.length > 1) {
					mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
				} else {
					if (!"".equals(arrSplitEqual[0])) {
						mapRequest.put(arrSplitEqual[0], "");
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return mapRequest;
	}
	
	/**
	 * 截取指定长度字符串
	 * @param content
	 * @param length
	 * @return
	 */
	public static String cutStr(String content, int length){
		try {
			if(Utils.isNotBlank(content)){
				if(content.length() > length){
					content = content.substring(0, length - 1) + "..."; 
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}
	
	public static String encodeUrl(String url){
		return url.replaceAll(" ", "%20").replaceAll("[|]", "%124");
	}
	
	public static void main(String args[]) {
////		System.out.println(formatDate_YYYY_MM_DD_HHMMSS(System.currentTimeMillis()));
//		System.out.println(md5("12ggfds45").length());
//		System.out.println(base64DecodeStr(base64EncodeStr("12345")));
		System.out.println(Utils.encrypt("国产液晶平板，首选【海信】，虽然这个企业售后很一般，但屏幕好，这个最重要其次【创维】的也很不错再次就是TCL了，TCL硬板的比较好！ 答 该问题还没有人回答，点击“我..."));
	}
}
