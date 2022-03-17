package com.lzlj.spider.browser;

import com.lzlj.config.ConfigHelper;
import com.lzlj.utils.Utils;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class PhantomJS extends Browser {
	private static final Logger LOG = LoggerFactory.getLogger(PhantomJS.class);
	private static final String PATH_KEY = "phantomjs.binary.path";
	private static final String USER_AGENT_KEY = "phantomjs.page.settings.userAgent";
	
	@Override
	protected WebDriver createDriver() {
		System.setProperty(PATH_KEY, ConfigHelper.get(DRIVER_URL_KEY));
		Map<String, Object> kvs = new HashMap<String, Object>();
		String args = ConfigHelper.get(PhantomJSDriverService.PHANTOMJS_CLI_ARGS);
		if (Utils.isNotBlank(args)) {
			kvs.put(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, args.split(","));
		}
		DesiredCapabilities capabilities = new DesiredCapabilities(kvs);
		String userAgent = ConfigHelper.get(USER_AGENT_KEY);
		if (Utils.isNotBlank(userAgent)) {
			capabilities.setCapability(USER_AGENT_KEY, userAgent);
		}
		capabilities.setCapability(CapabilityType.BROWSER_NAME, BrowserType.PHANTOMJS);
		capabilities.setCapability(CapabilityType.VERSION, "");
		capabilities.setCapability(CapabilityType.PLATFORM, Platform.ANY);
		return new PhantomJSDriver(capabilities);
	}
}
