package com.redpig.common.consts;

import java.io.File;

/**
 * Created by hetao on 2018/6/20.
 */
public interface Consts {
    String UTF_8 = "utf-8";
    String GBK = "gbk";
    String SLASH = "/";
    String DOT = ".";
    String BACKWARD_SLASH = "\\";
    String COLON = ":";
    String HTTP = "http://";
    String HTTPS = "https://";
    String FILE = "file://";

    String SIX_STAR = "******";
    String LOCAL_HOST = "localhost";

    char C_SLASH = '/';
    char C_BACKWARD_SLASH = '\\';
    char C_FILE_SEPERATOR = File.separatorChar;
    char C_LINE_BREAK = '\n';

    String FILE_SEPERATOR = String.valueOf(C_FILE_SEPERATOR);

    int SECOND = 1000;
    int MINUTE = SECOND * 60;
    int HOUR = MINUTE * 60;
    int DAY = HOUR * 24;

}
