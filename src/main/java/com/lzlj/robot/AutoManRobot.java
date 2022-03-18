package com.lzlj.robot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * AutoManRobot
 *
 * @author hetao
 * @date 2022/3/17
 */
public class AutoManRobot {
    private final Logger LOGGER = LoggerFactory.getLogger(AutoManRobot.class);
    private static final int INTERVAL = 500;
    private Robot robot;
    private List<Integer> pressKeys = new ArrayList<>();

    public AutoManRobot() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        String capturePath1 = "D:/wjcszs.png";
        String capturePath2 = "D:/laopo.png";

        AutoManRobot autoManRobot = new AutoManRobot();
        try {

            // 屏幕截图工具
//            autoManRobot.delayMouseScreenCapture(5000, capturePath1);

//            // 打开微信
            autoManRobot.pressKey(KeyEvent.VK_CONTROL, KeyEvent.VK_ALT, KeyEvent.VK_W);
            autoManRobot.delay(500);
            autoManRobot.releaseKey(KeyEvent.VK_CONTROL, KeyEvent.VK_ALT, KeyEvent.VK_W);
//
//            // 定位到截图位置，执行具体操作
            Point point1 = autoManRobot.getScreenPointByCapture(capturePath1);
            autoManRobot.mouseClick(point1);
//
//            autoManRobot.delay(500);
//            Point point2 = autoManRobot.getScreenPointByCapture(capturePath2);
////            point2.translate(50, 50);
//            autoManRobot.mouseClick(point2);
//
//            autoManRobot.delay(1000);
//            autoManRobot.inputText("你好,RedPig!测试2");
//            autoManRobot.delay(500);
//            autoManRobot.enter();
        } finally {
            autoManRobot.close();
        }
    }

    public void close() {
        releaseAllPressKey();
    }

    public void releaseAllPressKey() {
        for (Integer key : pressKeys) {
            robot.keyRelease(key);
        }
    }

    public void pressKey(int... key) {
        for (int i = 0; i < key.length; i++) {
            robot.keyPress(key[i]);
            pressKeys.add(key[i]);
        }
    }

    public void releaseKey(int... key) {
        for (int i = 0; i < key.length; i++) {
            robot.keyRelease(key[i]);
            pressKeys.remove(key);
        }
    }

    /**
     * 输入文字
     *
     * @param content 要输入的文字内容
     */
    public void inputText(String content) {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        clip.setContents(new StringSelection(content), null);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.delay(1000);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }

    /**
     * 读取内容
     *
     * @param point 位置
     * @return 返回读取内容
     */
    public String readText(Point point) {
        int x = (int) point.getX();
        int y = (int) point.getY();
        robot.mouseMove(x, y);
        robot.delay(INTERVAL);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.delay(INTERVAL);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_A);
        robot.keyRelease(KeyEvent.VK_A);
        robot.delay(INTERVAL);
        robot.keyPress(KeyEvent.VK_C);
        robot.keyRelease(KeyEvent.VK_C);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.delay(INTERVAL);
        return getSystemClipboard();
    }

    public void enter() {
        robot.keyPress( KeyEvent.VK_ENTER);
    }

    /**
     * 延迟执行
     *
     * @param time 时间ms
     */
    public void delay(int time) {
        robot.delay(time);
    }

    /**
     * 鼠标点击
     *
     * @param point 屏幕坐标
     */
    public void mouseClick(Point point) {
        if (point == null) {
            LOGGER.error("获取的屏幕坐标为空.");
            return;
        }
        int x = (int) point.getX();
        int y = (int) point.getY();
        robot.delay(200);
        robot.mouseMove(x, y);
        robot.delay(200);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.delay(200);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }

    /**
     * 鼠标滚动
     *
     * @param point 屏幕坐标
     */
    public void mouseWheel(Point point) {
        mouseWheel(point, 30);
    }

    public void mouseWheel(Point point, int times) {
        if (point == null) {
            LOGGER.error("获取的屏幕坐标为空.");
            return;
        }
        int x = (int) point.getX();
        int y = (int) point.getY();
        robot.delay(200);
        robot.mouseMove(x, y);
        robot.delay(200);
        for (int i = 0; i < times; i++) {
            robot.mouseWheel(4);
            robot.delay(1000);
        }
    }

    /**
     * 根据截图获取屏幕坐标
     *
     * @param capturePath 截图路径
     * @return 返回屏幕坐标
     */
    public Point getScreenPointByCapture(String capturePath) {
        Point point = null;
        try {
            BufferedImage image = getScreen(robot);
            BufferedImage capture = ImageIO.read(new File(capturePath));
            int width = image.getWidth();
            int height = image.getHeight();
            int captureWidth = capture.getWidth();
            int captureHeight = capture.getHeight();
            int targetX = -1;
            int targetY = -1;
            boolean hasFound = false;
            for (int x = 0; x < width - captureWidth; ++x) {
                if (!hasFound) {
                    for (int y = 0; y < height - captureHeight; ++y) {
                        if (isEqual(x, y, image, capture)) {
                            LOGGER.info("匹配到坐标，x:" + x + ", y:" + y);
                            targetX = x;
                            targetY = y;
                            hasFound = true;
                            break;
                        }
                    }
                }
            }
            if (targetX > 0 && targetY > 0) {
                point = new Point(targetX, targetY);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return point;
    }

    /**
     * 根据鼠标所在位置截取固定大小图片，用于定位屏幕位置
     *
     * @param delay      延迟执行的时间ms
     * @param outputPath 截图输出路径
     */
    public void delayMouseScreenCapture(int delay, String outputPath) {
        try {
            int times = delay / 1000;
            int i = times;
            while (i > 0) {
                LOGGER.info("请将鼠标放置在截图位置，" + i + "秒后执行截图...");
                Thread.sleep(1000);
                i--;
            }
            Point location = MouseInfo.getPointerInfo().getLocation();
            int x = (int) location.getX();
            int y = (int) location.getY();
            screenCapture(x + 20, y + 20, 20, 20, outputPath);
            LOGGER.info("截图成功，存放路径为：" + outputPath);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * 屏幕截图
     *
     * @param x          起始点x坐标
     * @param y          起始点y坐标
     * @param width      截取宽度
     * @param height     截取高度
     * @param outputPath 截图输出路径
     */
    public void screenCapture(int x, int y, int width, int height, String outputPath) {
        try {
            Point point = new Point(x, y);
            Dimension dimension = new Dimension(width, height);
            BufferedImage bufferedImage = robot.createScreenCapture(new Rectangle(point, dimension));
            ImageIO.write(bufferedImage, "png", new File(outputPath));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private BufferedImage getScreen(Robot robot) {
        return robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
    }

    /**
     * 判断坐标点位置图片是否相似
     */
    private boolean isEqual(int x, int y, BufferedImage image, BufferedImage capture) {
        int captureWidth = capture.getWidth();
        int captureHeight = capture.getHeight();

        int total = captureWidth * captureHeight;
        int count = 0;
        for (int m = 0; m < captureWidth; m++) {
            for (int n = 0; n < captureHeight; n++) {
                if (image.getRGB(x + m, y + n) == capture.getRGB(m, n)) {
                    count++;
                }
            }
        }
        Double d = count / (double) total;
        // 只需要50%相似即可
        return Double.compare(d, 0.5f) > 0;
    }

    public static String getSystemClipboard() {
        Clipboard sysClb = Toolkit.getDefaultToolkit().getSystemClipboard();
        try {
            Transferable t = sysClb.getContents(null);
            if (null != t && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                String text = (String) t.getTransferData(DataFlavor.stringFlavor);
                return text;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
