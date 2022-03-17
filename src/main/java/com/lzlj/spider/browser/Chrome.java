package com.lzlj.spider.browser;

import com.lzlj.config.ConfigHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Chrome extends Browser {
	private static final Logger LOG = LoggerFactory.getLogger(Chrome.class);
	
	private static final String PATH_KEY = "webdriver.chrome.driver";
	
	@Override
	protected WebDriver createDriver() {
		System.setProperty(PATH_KEY, ConfigHelper.get(DRIVER_URL_KEY));
		WebDriver driver = new ChromeDriver();
		return driver;
	}
}
