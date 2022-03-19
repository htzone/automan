package com.lzlj.spider.browser;

import com.lzlj.utils.Utils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * 浏览器抽象类
 */
public abstract class Browser {
	private static final Logger LOG = LoggerFactory.getLogger(Browser.class);
	protected static final String DRIVER_URL_KEY = "dd.driver.url";

	protected String latestUrl; 
	protected WebDriver driver;
	protected final AtomicBoolean alive = new AtomicBoolean(false);
	
	protected Browser() {
		this.ensureBrowser();
	}
	
	/**
	 * 截屏当前页面
	 * @return
	 */
	public byte[] snapshot() {
		this.ensureBrowser();
		if (this.driver != null) {
			try {
				WebDriver dv = this.driver;
				if (this.driver instanceof RemoteWebDriver) {
					dv = new Augmenter().augment(driver);
				}
				TakesScreenshot ts = (TakesScreenshot) dv;
				return ts.getScreenshotAs(OutputType.BYTES);
			} catch (UnreachableBrowserException e) {
				LOG.error("浏览器挂了", e);
				this.alive.set(false);
			}
		}
		return null;
	}
	
	/**
	 * 页面元素截屏
	 * @param by 选择器
	 * @param imgType 图片类型，如：JPG，默认png
	 * @return
	 */
	public byte[] elmSnapshot(By by, String imgType) {
		if (Utils.isNull(by)) {
			return null;
		}
		WebElement elm = this.findElement(by);
		if (Utils.isNull(elm)) {
			return null;
		}
		ByteArrayOutputStream baos = null;
		ByteArrayInputStream bais = null;
		try {
			Point point = elm.getLocation();
			Dimension dimensopm = elm.getSize();
			bais = new ByteArrayInputStream(snapshot());
			BufferedImage pageSnapshot = ImageIO.read(bais);
			BufferedImage elementSnapshot = pageSnapshot.getSubimage(point.getX(), point.getY(), dimensopm.getWidth(), dimensopm.getHeight());
			baos = new ByteArrayOutputStream(1024);
			ImageIO.write(elementSnapshot, Utils.isBlank(imgType) ? "png" : imgType, baos);
			baos.flush();
			return baos.toByteArray();
		} catch (UnreachableBrowserException e) {
			LOG.error("浏览器挂了", e);
			this.alive.set(false);
		} catch (Exception e) {
			LOG.error("元素截图失败", e);
		} finally {
			Utils.closeQuietly(baos);
			Utils.closeQuietly(bais);
		}
		return null;
	}
	
	/**
	 * 获取当前窗口源代码
	 * @return
	 */
	public String getPageSource() {
		try {
			this.ensureBrowser();
			return this.driver.getPageSource();
		} catch (UnreachableBrowserException e) {
			LOG.error("浏览器挂了", e);
			this.alive.set(false);
		}
		return "";
	}
	
	/**
	 * 等待元素被加载
	 * @param by
	 */
	public void waitUntilElementLocated(By by, long timeoutSecs) {
		(new WebDriverWait(driver, timeoutSecs)).until(ExpectedConditions.presenceOfElementLocated(by));
	}
	
	/**
	 * 导航到指定URL
	 * @param url   链接地址
	 */
	public void get(String url) {
		this.latestUrl = url;
		try {
			this.ensureBrowser();
			this.driver.get(url);
		} catch (UnreachableBrowserException e) {
			LOG.error("浏览器挂了", e);
			this.alive.set(false);
		}
	}
	
	/**
	 * 刷新当前页
	 */
	public void refresh() {
		try {
			this.ensureBrowser();
			this.driver.navigate().refresh();
		} catch (UnreachableBrowserException e) {
			LOG.error("浏览器挂了", e);
			this.alive.set(false);
		}
	}
	
	/**
	 * 获取网页标题
	 * @return
	 */
	public String getTitle() {
		try {
			this.ensureBrowser();
			return this.driver.getTitle();
		} catch (UnreachableBrowserException e) {
			LOG.error("浏览器挂了", e);
			this.alive.set(false);
		}
		return "";
	}
	
	/**
	 * 返回
	 */
	public void back() {
		try {
			this.ensureBrowser();
			this.driver.navigate().back();
		} catch (UnreachableBrowserException e) {
			LOG.error("浏览器挂了", e);
			this.alive.set(false);
		}
	}

	/**
	 * 打开一个新窗口，并跳转到指定url
	 * @param url 要跳转到的url
	 */
	public void open(String url) {
		this.latestUrl = url;
		try {
			this.ensureBrowser();
			String js = "window.open('" + url + "')";
			exeJs(js);
		} catch (UnreachableBrowserException e) {
			LOG.error("浏览器挂了", e);
			this.alive.set(false);
		}
	}

	/**
	 * 跳转到指定url
	 * @param url 要跳转到的url
	 */
	public void to(String url) {
		this.latestUrl = url;
		try {
			this.ensureBrowser();
			this.driver.navigate().to(url);
		} catch (UnreachableBrowserException e) {
			LOG.error("浏览器挂了", e);
			this.alive.set(false);
		}
	}
	
	/**
	 * 切换到frame
	 * @param frameIndex 从0开始
	 */
	public void switchTo(int frameIndex) {
		try {
			this.ensureBrowser();
			this.driver.switchTo().frame(frameIndex);
		} catch (UnreachableBrowserException e) {
			LOG.error("浏览器挂了", e);
			this.alive.set(false);
		}
	}

	/**
	 * 获取当前Window
	 */
	public String getWindow() {
		try {
			this.ensureBrowser();
			return this.driver.getWindowHandle();
		} catch (UnreachableBrowserException e) {
			LOG.error("浏览器挂了", e);
			this.alive.set(false);
		}
		return "";
	}

	/**
	 * 获取所有的Window
	 */
	public List<String> getWindows() {
		List<String> windows = new ArrayList<>();
		try {
			this.ensureBrowser();
			// driver返回的是有序set
			windows = new ArrayList<>(this.driver.getWindowHandles());
		} catch (UnreachableBrowserException e) {
			LOG.error("浏览器挂了", e);
			this.alive.set(false);
		}
		return windows;
	}

	/**
	 * 切换到Window
	 * @param window id属性或者name属性
	 */
	public void switchToWindow(String window) {
		try {
			this.ensureBrowser();
			this.driver.switchTo().window(window);
		} catch (UnreachableBrowserException e) {
			LOG.error("浏览器挂了", e);
			this.alive.set(false);
		}
	}

	/**
	 * 切换到frame
	 * @param frameIdOrName id属性或者name属性
	 */
	public void switchTo(String frameIdOrName) {
		try {
			this.ensureBrowser();
			this.driver.switchTo().frame(frameIdOrName);
		} catch (UnreachableBrowserException e) {
			LOG.error("浏览器挂了", e);
			this.alive.set(false);
		}
	}
	
	/**
	 * 切换到frame
	 * @param frameElm
	 */
	public void switchTo(WebElement frameElm) {
		try {
			this.ensureBrowser();
			this.driver.switchTo().frame(frameElm);
		} catch (UnreachableBrowserException e) {
			LOG.error("浏览器挂了", e);
			this.alive.set(false);
		}
	}
	
	/**
	 * 获取当前窗口链接地址
	 * @return
	 */
	public String getCurUrl() {
		try {
			this.ensureBrowser();
			return this.driver.getCurrentUrl();
		} catch (UnreachableBrowserException e) {
			LOG.error("浏览器挂了", e);
			this.alive.set(false);
		}
		return "";
	}
	
	/**
	 * 添加Cookie
	 * @param cookie 要添加的Cookie
	 */
	public void addCookie(Cookie cookie) {
		try {
			this.ensureBrowser();
			this.driver.manage().addCookie(cookie);
		} catch (UnreachableBrowserException e) {
			LOG.error("浏览器挂了", e);
			this.alive.set(false);
		}
	}
	
	/**
	 * 添加Cookie
	 * @param cookies 要添加的Cookie
	 */
	public void addCookie(String cookies) {
		try {
			if (Utils.isBlank(cookies)) {
				return;
			}
			this.ensureBrowser();
			String[] parts = cookies.split("; "), kvs;
			String key, value;
			Cookie cookie;
			for (String part : parts) {
				kvs = part.split("=");
				key = kvs[0];
				value = "";
				if (kvs.length >= 2) {
					value = kvs[1];
				}
				cookie = new Cookie(key, value);
				this.driver.manage().addCookie(cookie);
			}
		} catch (UnreachableBrowserException e) {
			LOG.error("浏览器挂了", e);
			this.alive.set(false);
		}
	}
	
	/**
	 * 删除Cookie
	 * @param name Cookie名字
	 */
	public void delCookie(String name) {
		try {
			this.ensureBrowser();
			this.driver.manage().deleteCookieNamed(name);
		} catch (UnreachableBrowserException e) {
			LOG.error("浏览器挂了", e);
			this.alive.set(false);
		}
	}
	
	/**
	 * 删除Cookie
	 * @param cookie 要删除的Cookie
	 */
	public void delCookie(Cookie cookie) {
		try {
			this.ensureBrowser();
			this.driver.manage().deleteCookie(cookie);
		} catch (UnreachableBrowserException e) {
			LOG.error("浏览器挂了", e);
			this.alive.set(false);
		}
	}
	
	/**
	 * 删除所有Cookie
	 */
	public void delAllCookie() {
		try {
			this.ensureBrowser();
			this.driver.manage().deleteAllCookies();
		} catch (UnreachableBrowserException e) {
			LOG.error("浏览器挂了", e);
			this.alive.set(false);
		}
	}
	
	/**
	 * 获取Cookie
	 * @param name Cookie名字
	 * @return
	 */
	public Cookie getCookie(String name) {
		try {
			this.ensureBrowser();
			return this.driver.manage().getCookieNamed(name);
		} catch (UnreachableBrowserException e) {
			LOG.error("浏览器挂了", e);
			this.alive.set(false);
		}
		return null;
	}
	
	/**
	 * 获取所有Cookie
	 * @return
	 */
	public Set<Cookie> getCookies() {
		try {
			this.ensureBrowser();
			return this.driver.manage().getCookies();
		} catch (UnreachableBrowserException e) {
			LOG.error("浏览器挂了", e);
			this.alive.set(false);
		}
		return null;
	}
	
	public String getCookieString() {
		Set<Cookie> cookies = getCookies();
		String cookieStr = "";
		int i = 0;
		for (Cookie cookie : cookies) {
			if (i > 0) {
				cookieStr += "; ";
			}
			cookieStr += cookie.getName() + "=" + cookie.getValue();
			i++;
		}
		return cookieStr;
	}
	
	/**
	 * 查找元素集合
	 * @param by 选择器
	 * @return
	 */
	public List<WebElement> findElements(By by) {
		if (Utils.isNull(by)) {
			return null;
		}
		try {
			this.ensureBrowser();
			return this.driver.findElements(by);
		} catch (NoSuchElementException e) {
			LOG.error("没有找到元素集合：" + by.toString());
		} catch (UnreachableBrowserException e) {
			LOG.error("浏览器挂了", e);
			this.alive.set(false);
		}
		return null;
	}
	
	/**
	 * 查找单个元素
	 * @param by 选择器
	 * @return
	 */
	public WebElement findElement(By by) {
		if (Utils.isNull(by)) {
			return null;
		}
		try {
			this.ensureBrowser();
			return this.driver.findElement(by);
		} catch (NoSuchElementException e) {
			LOG.error("没有找到元素：" + by.toString());
		} catch (UnreachableBrowserException e) {
			LOG.error("浏览器挂了", e);
			this.alive.set(false);
		}
		return null;
	}
	
	/**
	 * 拖拽你元素
	 * @param by  查找元素
	 * @param xOffset x偏移量
	 * @param yOffset y偏移量
	 */
	public void moveByOffset(By by, int xOffset, int yOffset) {
		try {
			this.ensureBrowser();
			WebElement elm = this.driver.findElement(by);
			if (Utils.isNull(elm)) {
				return;
			}
			new Actions(this.driver)
				.clickAndHold(elm)
				.moveByOffset(xOffset, yOffset).pause(3000)
				.release()
				.build()
				.perform();
		} catch (NoSuchElementException e) {
			LOG.error("没有找到元素：" + by.toString());
		} catch (UnreachableBrowserException e) {
			LOG.error("浏览器挂了", e);
			this.alive.set(false);
		}
	}
	
	/**
	 * 拖拽你元素
	 * @param elm  查找元素
	 * @param xOffset x偏移量
	 * @param yOffset y偏移量
	 */
	public void moveByOffset(WebElement elm, int xOffset, int yOffset) {
		try {
			this.ensureBrowser();
			if (Utils.isNull(elm)) {
				return;
			}
			new Actions(this.driver)
				.clickAndHold(elm)
				.moveByOffset(xOffset, yOffset).pause(3000)
				.release()
				.build()
				.perform();
		} catch (NoSuchElementException e) {
			LOG.error("没有找到元素：" + elm.toString());
		} catch (UnreachableBrowserException e) {
			LOG.error("浏览器挂了", e);
			this.alive.set(false);
		}
	}
	
	/**
	 * 输入值到文本框或者文本域
	 * @param by 选择器
	 * @param values 文本内容
	 */
	public void sendKeys(By by, String... values) {
		if (Utils.isNotNull(by)) {
			WebElement target = this.findElement(by);
			if (Utils.isNotNull(target)) {
				try {
					this.ensureBrowser();
					target.clear();
					target.sendKeys(values);
				} catch (UnreachableBrowserException e) {
					LOG.error("浏览器挂了", e);
					this.alive.set(false);
				}
			}
		}
	}
	
	/**
	 * 将浏览器滚动条增量滚动指定的y高度
	 * @param y 滚动的高度
	 */
	public void scrollBy(int y) {
		this.exeJs("window.scrollBy(0, " + y + ")");
	}
	
	/**
	 * 将浏览器滚动条滚动到指定的y位置，注意 Phantomjs 不适用
	 * @param y 要滚到的位置
	 */
	public void scrollTo(int y) {
		this.exeJs("document.documentElement.scrollTop=" + y);
	}

	/**
	 * 根据元素ID点击元素
	 * @param id 元素ID
	 */
	public void clickById(String id) {
		this.exeJs("document.getElementById('" + id + "').click()");
	}
	
	/**
	 * 点击指定元素
	 * @param by 选择器
	 */
	public void click(By by) {
		if (Utils.isNotNull(by)) {
			WebElement elm = this.findElement(by);
			if (Utils.isNotNull(elm)) {
				try {
					this.ensureBrowser();
					elm.click();
				} catch (UnreachableBrowserException e) {
					LOG.error("浏览器挂了", e);
					this.alive.set(false);
				} catch (Exception e) {
					LOG.error("点击元素失败", e);
				}
			}
		}
	}
	
	/**
	 * 点击指定元素，属性attr等于value的元素
	 * @param by 选择器
	 * @param attr 选择器
	 * @param value 选择器
	 */
	public void click(By by, String attr, String value) {
		if (Utils.isNotNull(by) || Utils.isBlank(attr) || Utils.isBlank(value)) {
			return;
		}
		List<WebElement> elms = this.findElements(by);
		if (Utils.isEmpty(elms)) {
			return;
		}
		this.ensureBrowser();
		for (WebElement elm : elms) {
			if (elm.getAttribute("attr").equals(value)) {
				try {
					elm.click();
				} catch (UnreachableBrowserException e) {
					LOG.error("浏览器挂了", e);
					this.alive.set(false);
					return;
				} catch (Exception e) {
					LOG.error("点击元素失败", e);
					return;
				}
			}
		}
	}
	
	/**
	 * 执行JS脚本
	 * @param js JS脚本
	 * @return
	 */
	public Object exeJs(String js) {
		try {
			this.ensureBrowser();
			return ((JavascriptExecutor) this.driver).executeScript(js);
		} catch (UnreachableBrowserException e) {
			LOG.error("浏览器挂了", e);
			this.alive.set(false);
		}
		return null;
	}
	
	/**
	 * 获取浏览器驱动对象
	 * @return
	 */
	public WebDriver getDriver() {
		return this.driver;
	}
	
	/**
	 * 关闭浏览器
	 */
	public void shutdown() {
		if (Utils.isNull(this.driver)) {
			return;
		}
		try {
			this.driver.manage().deleteAllCookies();
			this.driver.close();
			this.driver.quit();
			this.driver = null;
		} catch (UnreachableBrowserException e) {
			LOG.error("浏览器挂了", e);
		} finally {
			this.alive.set(false);
		}
	}
	
	/**
	 * 检测浏览器是否挂掉，若挂掉则重启一个
	 */
	private synchronized void ensureBrowser() {
		if (Utils.isNull(this.driver) || !this.alive.get()) {
			LOG.error("浏览器为空或者已经挂掉，新启动一个");
			try {
				this.driver = this.createDriver();
				this.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//				this.driver.manage().timeouts().pageLoadTimeout(12, TimeUnit.SECONDS);
//				this.driver.manage().timeouts().setScriptTimeout(12, TimeUnit.SECONDS);
				this.driver.manage().window().maximize();
				this.alive.set(true);
				if (Utils.isNotBlank(this.latestUrl)) {
					this.to(this.latestUrl);
				}
			} catch (Exception e) {
				LOG.error("启动浏览器失败", e);
			}
		}
	}
	
	/**
	 * 判断浏览器是否存活
	 * @return
	 */
	public boolean isAlive() {
		return this.alive.get();
	}
	
	/**
	 * 创建浏览器驱动
	 * @return
	 */
	protected abstract WebDriver createDriver();
}
