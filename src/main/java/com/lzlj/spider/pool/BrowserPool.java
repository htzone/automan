package com.lzlj.spider.pool;

import com.lzlj.spider.browser.Browser;
import com.lzlj.utils.Utils;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 浏览器池(减少内存开销)
 */
public class BrowserPool {
	private static final Logger LOG = LoggerFactory.getLogger(BrowserPool.class);
	
	private static ObjectPool<Browser> browserPool;
	
	static {
		try {
			GenericObjectPoolConfig config = new GenericObjectPoolConfig();
			config.setMinIdle(1);
			config.setMaxIdle(3);
			config.setMaxTotal(6);
			browserPool = new GenericObjectPool<Browser>(new BrowserPoolObjectFactory(), config);
		} catch (Exception e) {
			LOG.error("初始化浏览器对象池失败", e);
		}
	}
	
	public static Browser borrowBrowser() {
		try {
			return browserPool.borrowObject();
		} catch (Exception e) {
			LOG.error("获取浏览器对象失败", e);
			return null;
		}
	}
	
	public static void returnBrowser(Browser browser) {
		try {
			browserPool.returnObject(browser);
		} catch (Exception e) {
			LOG.error("返回浏览器对象失败", e);
		}
	}
	
	public static boolean hasIdle() {
		return browserPool.getNumIdle() > 0;
	}
	
	public static boolean hasActive() {
		return browserPool.getNumActive() > 0;
	}
	
	public static void close() {
		if (Utils.isNotNull(browserPool)) {
			try {
				browserPool.close();
			} catch (Exception e) {
				LOG.error("关闭浏览器对象池失败", e);
			}
		}
	}
}
