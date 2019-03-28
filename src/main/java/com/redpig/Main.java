package com.redpig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

/**
 * Created by hetao on 2018/6/19.
 */
public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) throws Exception{
//        Robot robot = new Robot();
//        PaxySpider spider = new PaxySpider();
//        spider.autoClickArticles();
//        new ClassPathXmlApplicationContext("context.xml");
////        LOG.info(SqlBuildHelper.createTable(MansResource.class, SqlBuildHelper.DB_MYSQL));
//        BaseService baseService = AppCtxHolder.getBean("baseService");
//        MansResource mansResource = new MansResource();
//        mansResource.setTitle("test1");
//        mansResource.setDescription("test2");
//        baseService.update(mansResource);

//        robot.keyPress(KeyEvent.VK_TAB);
//        robot.keyPress(KeyEvent.VK_ENTER);
        robotTest();
    }

    private static void robotTest() throws Exception{
        Robot robot = new Robot();
//        Rectangle rect = new Rectangle(0, 0, 100, 100);
//        BufferedImage image = robot.createScreenCapture(rect);
//        System.out.println(image.getHeight());

//        robot.keyPress(KeyEvent.VK_CONTROL);
//        robot.keyPress(KeyEvent.VK_E);
//        robot.keyRelease(KeyEvent.VK_CONTROL);
//        robot.keyRelease(KeyEvent.VK_E);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        System.out.println(toolkit.getScreenSize().width);
        System.out.println(toolkit.getScreenSize().height);
    }
}
