package com.lzlj.spider.browser;

import com.lzlj.config.ConfigHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IE extends Browser {
	private static final Logger LOG = LoggerFactory.getLogger(IE.class);

	private static final String PATH_KEY = "webdriver.ie.driver";
	
	@Override
	protected WebDriver createDriver() {
		System.setProperty(PATH_KEY, ConfigHelper.get(DRIVER_URL_KEY));
		DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
		capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		WebDriver driver = new InternetExplorerDriver(capabilities);
		return driver;
	}
}
