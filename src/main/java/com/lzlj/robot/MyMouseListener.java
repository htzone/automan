package com.lzlj.robot;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * MyMouseListener
 *
 * @author hetao
 * @date 2022/3/17
 */
public class MyMouseListener implements MouseMotionListener {
    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int x=e.getX();
        int y=e.getY();
        String s="当前鼠标坐标:"+x+','+y;
        MouseMove.lab.setText(s);
    }
}
