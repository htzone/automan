package com.lzlj;


import com.lzlj.config.ConfigHelper;
import com.lzlj.constant.AppConstant;
import com.lzlj.utils.BatUtils;
import com.lzlj.utils.PathUtils;
import com.lzlj.utils.Utils;
import com.lzlj.utils.WarUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        LOGGER.info("start...");
        buildProject();
        replaceConfigFiles();
        LOGGER.info("replace success!!!");
    }

    private static void buildProject() throws Exception {
        if ("true".equals(ConfigHelper.get("dd.build"))) {
            String buildFilePath = copyBuildFileToProjectDir();
            BatUtils.exec(buildFilePath, new File(buildFilePath).getParent(), line -> line.contains("BUILD SUCCESS"));
            Utils.delayMillseconds(3000);
        }
    }

    private static String copyBuildFileToProjectDir() throws Exception {
        String batFileName = "/build.bat";
        String projectPath = new File("").getCanonicalPath();
        String batFilePath = PathUtils.combine(projectPath, "config", batFileName);
        File file = new File(batFilePath);
        InputStream inputStream = null;
        if (file.exists()) {
            inputStream = new FileInputStream(batFilePath);
        } else {
            inputStream = Main.class.getResourceAsStream(batFileName);
        }
        String projectDir = ConfigHelper.get("dd.project.dir");
        String buildFilePath = PathUtils.combine(projectDir, "lzlj-parent", batFileName);
        FileUtils.copyInputStreamToFile(inputStream, new File(buildFilePath));
        return buildFilePath;
    }

    private static void replaceConfigFiles() throws Exception {
        replaceConfigFiles(AppConstant.APP_SERVICE_ORDERS);
        replaceConfigFiles(AppConstant.APP_WEB_ORDERS);
        replaceConfigFiles(AppConstant.APP_SERVICE_USERS);
        replaceConfigFiles(AppConstant.APP_WEB_USERS);
        replaceConfigFiles(AppConstant.APP_SERVICE_FEE);
        replaceConfigFiles(AppConstant.APP_WEB_FEE);
        replaceConfigFiles(AppConstant.APP_SERVICE_API);
        replaceConfigFiles(AppConstant.APP_SERVICE_CONTRACT);
        replaceConfigFiles(AppConstant.APP_WEB_CONTRACT);
        replaceConfigFiles(AppConstant.APP_SERVICE_ESB);
        replaceConfigFiles(AppConstant.APP_WEB_MOBILE_SHOP);
        replaceConfigFiles(AppConstant.APP_WEB_MOBILE_USERS);
        replaceConfigFiles(AppConstant.APP_WEB_SHOP);
        replaceConfigFiles(AppConstant.APP_WEB_WORKFLOW);
    }

    private static void replaceConfigFiles(String appName) throws Exception {
        // 获取war包路径
        String projectDir = ConfigHelper.get("dd.project.dir");
        String oldWarPath = PathUtils.combine(projectDir, appName, "target", appName + ".war");
        LOGGER.info("oldWarPath:" + oldWarPath);

        // 解压war包到指定目录
        String unzipTargetDir = ConfigHelper.get("dd.unzip.target.dir");
        String unzipTargetPath = PathUtils.combine(unzipTargetDir, appName);
        LOGGER.info("unzipTargetPath:" + unzipTargetPath);
        FileUtils.deleteQuietly(new File(unzipTargetPath));
        WarUtils.unzip(oldWarPath, unzipTargetPath);

        // 删除解压包里面的配置文件
        String relativePropPath = "/WEB-INF/classes/";
        String destConfigDir = unzipTargetPath + relativePropPath;
        LOGGER.info("destConfigDir:" + destConfigDir);
        Collection<File> oldConfigFiles = FileUtils.listFiles(new File(destConfigDir), new String[] {"properties", "xml", "json"}, false);
        for (File file : oldConfigFiles) {
            LOGGER.info("---oldConfigFile:" + file.getCanonicalPath());
            FileUtils.deleteQuietly(file);
        }

        // 替换配置文件
        String env = "test".equals(ConfigHelper.get("dd.environment")) ? "测试系统" : "生产系统";
        String srzConfigDir = PathUtils.combine(projectDir, "lzlj-com/src/main/resources/config/服务器环境配置文件", env, appName);
        LOGGER.info("srzConfigDir:" + srzConfigDir);
        Collection<File> srcConfigFiles = FileUtils.listFiles(new File(srzConfigDir), new String[] {"properties", "xml", "json"}, false);
        for (File file : srcConfigFiles) {
            String destConfigFilePath = PathUtils.combine(destConfigDir, file.getName());
            LOGGER.info("---destConfigFilePath:" + file.getCanonicalPath());
            FileUtils.copyFile(file, new File(destConfigFilePath));
        }

        // 压缩war包，生成新war包
        String destZipPath = PathUtils.combine(unzipTargetDir, appName + ".war");
        FileUtils.deleteQuietly(new File(destZipPath));
        WarUtils.zip(destZipPath, unzipTargetPath);
    }
}
