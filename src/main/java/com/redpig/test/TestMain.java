package com.redpig.test;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by hetao on 2019/3/28.
 */
public class TestMain {
    public static void main(String[] args) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        SAXHandler handler = new SAXHandler();
        String filePath = TestMain.class.getClassLoader().getResource("book.xml").getPath();
        parser.parse(filePath, handler);
    }
}
