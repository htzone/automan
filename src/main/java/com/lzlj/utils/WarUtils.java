package com.lzlj.utils;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Iterator;

/**
 * WarUtils
 *
 * @author hetao
 * @date 2022/1/26
 */
public class WarUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(WarUtils.class);

    public static void main(String[] args) {
//        unzip("E:/test/lzlj-orders-web.war", "E:/test/unzip");
        zip("E:/test/lzlj-orders-web1.war", "E:/test/unzip");
    }

    public static void unzip(String warPath, String unzipPath) {
        File warFile = new File(warPath);
        ArchiveInputStream in = null;
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(warFile));
            in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR,
                    bufferedInputStream);
            JarArchiveEntry entry = null;
            while ((entry = (JarArchiveEntry) in.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    new File(unzipPath, entry.getName()).mkdir();
                } else {
                    OutputStream out = null;
                    try {
                        out = FileUtils.openOutputStream(new File(unzipPath, entry.getName()));
                        IOUtils.copy(in, out);
                    } finally {
                        out.close();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("未找到war文件", e);
        } catch (ArchiveException e) {
            LOGGER.error("不支持的压缩格式", e);
        } catch (IOException e) {
            LOGGER.error("文件写入发生错误", e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    public static void zip(String destFile, String zipDir) {
        File outFile = new File(destFile);
        ArchiveOutputStream out = null;
        try {
            outFile.createNewFile();
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(outFile));
            out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, bufferedOutputStream);

            if (zipDir.charAt(zipDir.length() - 1) != '/') {
                zipDir += '/';
            }

            Iterator<File> files = FileUtils.iterateFiles(new File(zipDir), null, true);
            while (files.hasNext()) {
                File file = files.next();
                ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, file.getPath().replace(
                        zipDir.replace("/", "\\"), ""));
                out.putArchiveEntry(zipArchiveEntry);
                IOUtils.copy(new FileInputStream(file), out);
                out.closeArchiveEntry();
            }
        } catch (IOException e) {
            LOGGER.error("创建文件失败", e);
        } catch (ArchiveException e) {
            LOGGER.error("不支持的压缩格式", e);
        }
        finally {
            try {
                out.finish();
                out.close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }
}
