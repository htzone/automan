package com.lzlj.utils;

/**
 * MyFileUtils
 *
 * @author hetao
 * @date 2022/2/16
 */
public class PathUtils {
    public static final String SEPARATOR = "/";

    public static void main(String[] args) {
        System.out.println(PathUtils.combine("/test1", "test2", "test3/", "/test4/"));
    }

    public static String combine(String... args) {
        return combine(false, args);
    }

    /**
     * 拼接路径
     *
     * @param end 结尾是否输出分割符
     * @param args 路径参数
     * @return 返回拼接路径
     */
    public static String combine(boolean end, String... args) {
        StringBuilder result = new StringBuilder();
        if (args.length == 0) {
            throw new IllegalArgumentException("参数个数小于1");
        }
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith(SEPARATOR)) {
                arg = arg.substring(1);
            }
            if (arg.endsWith(SEPARATOR)) {
                arg = arg.substring(0, arg.length() - 1);
            }
            result.append(arg);
            if (i == (args.length - 1) && !end) {
                continue;
            }
            result.append(SEPARATOR);
        }
        return result.toString();
    }

}
