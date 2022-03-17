package com.lzlj;

import com.lzlj.spider.browser.Browser;
import com.lzlj.spider.pool.BrowserPool;
import com.lzlj.utils.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;

/**
 * Test
 *
 * @author hetao
 * @date 2022/1/26
 */
public class Test {
    public static void main(String[] args) throws Exception {
//        spider();
//        String businessKey = "UnconventionalContract.20220223151440C5BDA944F910FCF7E7";
//        String businessKey = "UnconventionalContract20220223151440C5BDA944F910FCF7E7";
//
//        System.out.println(businessK
//        ey.substring(businessKey.indexOf(".") + 1));

//        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
//        Transferable tText = null;
//        String[] lists ={"test","test2"};
//
//        robot.delay(5000);//延迟十秒，主要是为了预留出打开窗口的时间，括号内的单位为毫秒
//        for (int i = 0; i < lists.length; i++) {
//            tText = new StringSelection(lists[i]); //自己定义就需要把这行注释，下行取消注释
////            tText = new StringSelection("爱你每一天");//如果爱得深，把这行取消注释，把内容更换掉你自己想说的
//            clip.setContents(tText, null);
//            robot.keyPress(KeyEvent.VK_CONTROL);
//            robot.keyPress(KeyEvent.VK_V);
//            //robot.keyRelease(KeyEvent.VK_CONTROL);
//            robot.delay(3000);
//            robot.keyRelease(KeyEvent.VK_CONTROL);
//            robot.keyPress( KeyEvent.VK_ENTER);
//            robot.delay(5000);
//        }

        // 获取鼠标点击坐标
//        JFrame fm=new JFrame("鼠标坐标测试");
//        JPanel fp=new JPanel();
//        fp.addMouseMotionListener(new MyMouseListener());//对在面板上的鼠标移动进行监听。
//        Container con=fm.getContentPane();
//        fp.add(MouseMove.lab);
//        con.add(fp);
//        fm.setSize(500,400);
//        fm.setVisible(true);
//        fm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        while(true) {
            Point location = MouseInfo.getPointerInfo().getLocation();
            double x = location.getX();
            double y = location.getY();
            System.out.println("x = " + x);
            System.out.println("y = " + y);
            try {
                Thread.sleep(1000);
            }catch (Exception e) {
            }
            //x1298y1055
        }

        // 移动然后点击
//        Robot robot = new Robot();
//        robot.mouseMove(1298, 1055);
//        robot.delay(500);
//        robot.mousePress(InputEvent.BUTTON1_MASK);
//        robot.mouseRelease(InputEvent.BUTTON1_MASK);
//        robot.delay(500);

        // 自动画画
//        drawTest();

        // 屏幕截图
//        try {
//            Thread.sleep(5000);
//        }catch (Exception e) {
//        }
//        Point location = MouseInfo.getPointerInfo().getLocation();
//        double x = location.getX();
//        double y = location.getY();
//        System.out.println("x = " + x);
//        System.out.println("y = " + y);

        // x1286y1048

        // 输出截图
//        Robot robot = new Robot();
////        Point point = new Point(1286, 1048);
//        Point point = new Point(70, 461);
//        Dimension dimension = new Dimension(20, 20);
//        BufferedImage bufferedImage = robot.createScreenCapture(new Rectangle(point, dimension));
//        ImageIO.write(bufferedImage, "png", new File("D:/wechatdyh.png"));

//        Robot robot = new Robot();
//        BufferedImage image = getScreen(robot);
//        BufferedImage img = ImageIO.read(new File("D:/wechat.png"));
//        int width = image.getWidth();
//        int height = image.getHeight();
//
//        int targetX = -1;
//        int targetY = -1;
//
//        boolean hasFind = false;
//        for (int x = 0; x < width - 20; ++x) {
//            if (!hasFind) {
//                for (int y = 0; y < height - 20; ++y) {
//                    if (isEqual(x, y, image, img)) {
//                        System.out.println("匹配到x:" + x + ", y:" + y);
//                        targetX = x;
//                        targetY = y;
//                        hasFind = true;
//                        break;
//                    }
//                }
//            }
//        }
//
//        if (targetX > 0 && targetY > 0) {
//            robot.mouseMove(targetX, targetY);
//            robot.delay(500);
//            robot.mousePress(InputEvent.BUTTON1_MASK);
//            robot.mouseRelease(InputEvent.BUTTON1_MASK);
//            robot.delay(1000);
//            for (int i = 0; i < 30; i++) {
//                robot.mouseWheel(2);
//                robot.delay(500);
//            }
//        }
    }

    public static BufferedImage getScreen(Robot robot) {
        return robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
    }

    public static boolean isEqual(int x, int y, BufferedImage image, BufferedImage point) {
        int pointW = point.getWidth();
        int pointY = point.getHeight();

        int total = pointW * pointY;
        int count = 0;
        for (int m = 0; m < pointW; m++) {
            for (int n = 0; n < pointY; n++) {
                if (image.getRGB(x + m, y + n) == point.getRGB(m, n)) {
                    count++;
                }
            }
        }
        Double d = count / (double) total;
        return Double.compare(d, 0.5f) > 0;
    }

    private static void drawTest() throws AWTException {
        Robot robot = new Robot();
        robot.delay(3000); //运行代码后，暂停三秒，留够时间去打开电脑自带的画板，并点击形状里面的椭圆形

        //1111111111111111111111
        int i = 10;
        while (i-- > 0) {
            robot.mouseMove(400, 300);
        }
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.delay(100);       //这里延迟0.1s，可以看到动态画的过程
        i = 10;
        while (i-- > 0) {
            robot.mouseMove(650, 550);
        }
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.delay(1000);       //画完一个圆停止0.2s，否则因为计算机执行速度太快，看不到动态作图的过程

        //222222222222222222222
        i = 10;
        while (i-- > 0) {
            robot.mouseMove(0, 300);
        }
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK); //画完一个圆后在圆外面点一下鼠标，否则回拖动画的圆到下一个位置
        i = 10;
        while (i-- > 0) {
            robot.mouseMove(600, 300);
        }
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.delay(100);       //这里延迟0.1s，可以看到动态画的过程
        i = 10;
        while (i-- > 0) {
            robot.mouseMove(850, 550);
        }
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.delay(1000);       //画完一个圆停止0.2s，否则因为计算机执行速度太快，看不到动态作图的过程

        //3333333333333333333333333
        i = 10;
        while (i-- > 0) {
            robot.mouseMove(0, 300);
        }
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK); //画完一个圆后在圆外面点一下鼠标，否则回拖动画的圆到下一个位置
        i = 10;
        while (i-- > 0) {
            robot.mouseMove(800, 300);
        }
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.delay(100);       //这里延迟0.2s，可以看到动态画的过程
        i = 10;
        while (i-- > 0) {
            robot.mouseMove(1050, 550);
        }
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.delay(200);       //画完一个圆停止0.1s，否则因为计算机执行速度太快，看不到动态作图的过程

        //44444444444444444444444444
        i = 10;
        while (i-- > 0) {
            robot.mouseMove(0, 300);
        }
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK); //画完一个圆后在圆外面点一下鼠标，否则回拖动画的圆到下一个位置
        i = 10;
        while (i-- > 0) {
            robot.mouseMove(500, 425);
        }
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.delay(100);       //这里延迟0.1s，可以看到动态画的过程
        i = 10;
        while (i-- > 0) {
            robot.mouseMove(750, 675);
        }
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.delay(200);       //画完一个圆停止0.2s，否则因为计算机执行速度太快，看不到动态作图的过程

        //555555555555555555555555
        i = 10;
        while (i-- > 0) {
            robot.mouseMove(0, 300);
        }
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK); //画完一个圆后在圆外面点一下鼠标，否则回拖动画的圆到下一个位置
        i = 10;
        while (i-- > 0) {
            robot.mouseMove(700, 425);
        }
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.delay(100);       //这里延迟0.1s，可以看到动态画的过程
        i = 10;
        while (i-- > 0) {
            robot.mouseMove(950, 675);
        }
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.delay(200);       //画完一个圆停止0.2s，否则因为计算机执行速度太快，看不到动态作图的过程
    }

    private static void spider() {
        String url = "https://www.baidu.com/s?ie=UTF-8&wd=%E6%B3%B8%E5%B7%9E%E8%80%81%E7%AA%96";
        Browser browser = null;
        try {
            browser = BrowserPool.borrowBrowser();
            browser.to(url);
            Utils.delayMillseconds(2000);
            while (true) {
                WebElement elem = browser.findElement(By.cssSelector("span.price_2jYb9"));
                System.out.println(elem.getText());
                Utils.delayMillseconds(300000);
                browser.refresh();
            }
//            for (int i = 0; i < 3; i++) {
//                for (int j = 950; j < 5000; j += 950) {
//                    browser.scrollBy(950);
//                    Utils.delayMillseconds(1000);
//                }
//                WebElement elem = browser.findElement(By.cssSelector("div.page-inner_2jZi2 a.n"));
//                elem.click();
//                Utils.delayMillseconds(2000);
//            }
        } finally {
            if (browser != null) {
                BrowserPool.returnBrowser(browser);
            }
        }
    }
}
