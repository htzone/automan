package com.lzlj.spider.browser;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Firefox extends Browser {
	private static final Logger LOG = LoggerFactory.getLogger(Firefox.class);
	@Override
	protected WebDriver createDriver() {
		WebDriver driver = new FirefoxDriver();
		return driver;
	}
}
