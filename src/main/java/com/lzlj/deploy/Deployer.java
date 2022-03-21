package com.lzlj.deploy;

import com.lzlj.Main;
import com.lzlj.spider.browser.Browser;
import com.lzlj.spider.pool.BrowserPool;
import com.lzlj.utils.Utils;
import com.sun.java.swing.plaf.windows.resources.windows;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Deployer
 *
 * @author hetao
 * @date 2022/3/18
 */
public class Deployer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Deployer.class);

    public static void main(String[] args) {
        deploy();
    }

    private static void deploy() {
        String appListPageUrl = "https://edasnext.console.aliyun.com/?spm=5176.12818093.favorite.dedas.3be916d0kPVIo9#/app?regionNo=cn-shanghai&resourceGroupId=all";
        String loginUrl = "https://signin.aliyun.com/login.htm#/main";
        Browser browser = null;
        try {
            browser = BrowserPool.borrowBrowser();
            browser.to(appListPageUrl);
            Utils.delayMillseconds(2000);
            // 检查是否登录
            if (!isLogin(browser)) {
                LOGGER.info("未登录，需重新登录！");
                browser.to(loginUrl);
                Utils.delayMillseconds(2000);
                WebElement usernameElem = browser.findElement(By.cssSelector("input[name=username]"));
                usernameElem.sendKeys("ddxt@1368028871311170.onaliyun.com");
                Utils.delayMillseconds(2000);

                // 检查是否需要拖拽验证
                if (isExistElem(browser, "span#nc_1_n1z")) {
                    LOGGER.info("需要拖拽验证！");
                    WebElement dragElem = browser.findElement(By.cssSelector("span#nc_1_n1z"));
                    browser.moveByOffset(dragElem, 500, 0);
                    Utils.delayMillseconds(3000);
                }

                WebElement nextButton = browser.findElement(By.cssSelector("form.next-form button.next-btn"));
                nextButton.click();
                Utils.delayMillseconds(1000);

                WebElement passwordElem = browser.findElement(By.cssSelector("input[name=password]"));
                passwordElem.sendKeys("I'mddxt@lz");
                Utils.delayMillseconds(2000);

                // 检查是否需要拖拽验证
                if (isExistElem(browser, "span#nc_1_n1z")) {
                    LOGGER.info("需要拖拽验证！");
                    WebElement dragElem = browser.findElement(By.cssSelector("span#nc_1_n1z"));
                    browser.moveByOffset(dragElem, 500, 0);
                    Utils.delayMillseconds(3000);
                }

                WebElement submitButton = browser.findElement(By.cssSelector("form.next-form button[type=submit]"));
                submitButton.click();
                Utils.delayMillseconds(5000);

                if (!isLogin(browser)) {
                    LOGGER.error("未登录成功，请检查页面是否发生了改变");
                    return;
                } else {
                    LOGGER.info("登录成功！");
                }
            } else {
                LOGGER.info("已登录！");
            }

            browser.to("https://edasnext.console.aliyun.com/?spm=5176.12818093.favorite.dedas.3be916d0kPVIo9#/detail/cdc56920-d8ac-4fcc-81e3-e4b11d1d6f33/deployApp?deployType=normal&regionNo=cn-shanghai:lzljorderstest&regionId=cn-shanghai:lzljorderstest&appName=lzlj-api-service&dockerize=false&groupNo=7aee4f7a-d09e-4692-9e98-3350e55c169a&clusterType=2&applicationType=War&developType=Pandora");
            WebElement fileUpload = browser.findElement(By.cssSelector("div#upload input[name=fileItem]"));
            fileUpload.sendKeys("D:\\lzlj-orders-service.war");
            Utils.delayMillseconds(5000);
//            WebElement uploadButton = browser.findElement(By.cssSelector("div#upload button[type=button]"));
//            uploadButton.click();

            while (isExistElem(browser, "div[role=progressbar]")) {
                LOGGER.info("检测到文件还在上传中...");
                Utils.delayMillseconds(10000);
            }
            Utils.delayMillseconds(2000);
            WebElement versionButton = browser.findElement(By.cssSelector("form.el-form div[data-spm-click=gostr=/aliyun;locaid=d_0;]"));
            versionButton.click();

            // 获取applist
//            List<WebElement> appList = browser.findElements(By.cssSelector("table.el-table__body tbody tr"));
//            for (WebElement item : appList) {
//                WebElement titleElem = item.findElement(By.cssSelector("td.el-table_1_column_2 span"));
//                String text = titleElem.getText();
//                if (Utils.isNotBlank(text) && text.contains("lzlj-workflow")) {
//                    titleElem.click();
//                    break;
//                }
//            }
//            Utils.delayMillseconds(4000);

        } finally {
            if (browser != null) {
                BrowserPool.returnBrowser(browser);
            }
        }
    }

    private static boolean isLogin(Browser browser) {
        return isExistElem(browser, "div[data-spm=top-nav]");
    }

    private static boolean isExistElem(Browser browser, String selector) {
        boolean isExist = true;
        try {
            browser.findElement(By.cssSelector(selector));
        } catch (NoSuchElementException e) {
            isExist = false;
        }
        return isExist;
    }

}
