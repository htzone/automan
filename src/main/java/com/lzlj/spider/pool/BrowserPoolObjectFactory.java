package com.lzlj.spider.pool;

import com.lzlj.config.ConfigHelper;
import com.lzlj.spider.browser.*;
import com.lzlj.utils.Utils;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrowserPoolObjectFactory implements PooledObjectFactory<Browser> {
	private static final Logger LOG = LoggerFactory.getLogger(BrowserPoolObjectFactory.class);

	private static final String[] browserClassNames = {
		PhantomJS.class.getName(),
		Chrome.class.getName(),
		Firefox.class.getName(),
		IE.class.getName()
	};
	
	@Override
	public PooledObject<Browser> makeObject() throws Exception {
		PooledObject<Browser> po = new DefaultPooledObject<Browser>(this.doMakeObject());
		return po;
	}

	@Override
	public void destroyObject(PooledObject<Browser> p) throws Exception {
		p.getObject().shutdown();
	}

	@Override
	public boolean validateObject(PooledObject<Browser> p) {
		return p.getObject().isAlive();
	}

	@Override
	public void activateObject(PooledObject<Browser> p) throws Exception {
		// ignored
	}

	@Override
	public void passivateObject(PooledObject<Browser> p) throws Exception {
		// ignored
	}
	
	@SuppressWarnings("unchecked")
	private Browser doMakeObject() throws Exception {
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
			return clazz.newInstance();
		}
		throw new RuntimeException("创建池对象失败");
	}
}
