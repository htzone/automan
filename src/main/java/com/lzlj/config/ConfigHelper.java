package com.lzlj.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * ConfigHelper
 *
 * @author hetao
 * @date 2022/1/26
 */
public class ConfigHelper {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigHelper.class);
    private static final Properties props = new Properties();
    private static final String PROJECT_CONFIG_DIR = "/config";
    private static final String PROJECT_CONFIG_FILE = "/config.properties";
    private static final String CONFIG_CHARSET = "utf-8";
    static {
        Reader reader = null;
        try {
            String projectPath = new File("").getCanonicalPath();
            String propPath = projectPath + PROJECT_CONFIG_DIR + PROJECT_CONFIG_FILE;
            File file = new File(propPath);
            if (file.exists()) {
                reader = new InputStreamReader(new FileInputStream(propPath), CONFIG_CHARSET);
            } else {
                reader = new InputStreamReader(ConfigHelper.class.getResourceAsStream(PROJECT_CONFIG_FILE), CONFIG_CHARSET);
            }
            props.load(reader);
        } catch (Exception e) {
            LOG.error("加载配置文件config.properties失败", e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

    public static String get(String key) {
        return props.getProperty(key, "");
    }

    public static Integer getInt(String key) {
        String value = get(key);
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return null;
        }
    }
}
