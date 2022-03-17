package com.lzlj.spider.pool;

import com.lzlj.config.ConfigHelper;
import com.lzlj.spider.browser.*;
import com.lzlj.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 浏览器缓存池
 */
public class BrowserCache {
	private static final Logger LOG = LoggerFactory.getLogger(BrowserCache.class);
	private static final Map<String, Browser> cache = new HashMap<String, Browser>();
	
	private static final String[] browserClassNames = {
		PhantomJS.class.getName(),
		Chrome.class.getName(),
		Firefox.class.getName(),
		IE.class.getName()
	};
	
	@SuppressWarnings("unchecked")
	public static Browser get(String key) {
		Browser browser = cache.get(key);
		if (Utils.isNull(browser)) {
			String driverUrl = ConfigHelper.get("dd.driver.url").toUpperCase(), browserClassName = "";
			if (Utils.isNotBlank(driverUrl)) {
				String driverName = driverUrl.substring(driverUrl.lastIndexOf("/") + 1);
				if (driverName.contains("PHANTOMJS")) {
					browserClassName = browserClassNames[0];
				} else if (driverName.contains("CHROME")) {
					browserClassName = browserClassNames[1];
				} else if (driverName.contains("FIREFOX")) {
					browserClassName = browserClassNames[2];
				} else if (driverName.contains("IE")) {
					browserClassName = browserClassNames[3];
				}
			}
			if (Utils.isBlank(browserClassName)) {
				browserClassName = browserClassNames[2];
			}
			Class<Browser> clazz = null;
			try {
				clazz = (Class<Browser>) Class.forName(browserClassName);
			} catch (Exception e) {
				LOG.error("加载类" + browserClassName + "失败", e);
			}
			if (Utils.isNotNull(clazz)) {
				try {
					browser = clazz.newInstance();
				} catch (Exception e) {
					LOG.error("实例化" + browserClassName + "失败", e);
				}
				cache.put(key, browser);
			}
		}
		return browser;
	}
	
	public static void refresh(String key) {
		Browser browser = cache.get(key);
		if (Utils.isNotNull(browser)) {
			browser.refresh();
		}
	}
	
	public static String getCookie(String key) {
		Browser browser = cache.get(key);
		if (Utils.isNotNull(browser)) {
			return browser.getCookieString();
		}
		return "";
	}
}
