package com.lzlj.utils;

import com.lzlj.Main;
import com.lzlj.inter.FinishCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * BatUtils
 *
 * @author hetao
 * @date 2022/2/17
 */
public class BatUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void exec(String batFile, String dir) throws InterruptedException {
        InputStream in = null;
        BufferedReader br = null;
        InputStreamReader isr = null;
        try {
            Process p = Runtime.getRuntime().exec(batFile, null, new File(dir));
            in = p.getInputStream();
            isr = new InputStreamReader(in, "GBK");
            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                LOGGER.info(line);
            }
            p.waitFor();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // ignored
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // ignored
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    // ignored
                }
            }
        }
    }

    public static void exec(String batFile, String dir, FinishCheck finishCheck) throws InterruptedException {
        InputStream in = null;
        BufferedReader br = null;
        InputStreamReader isr = null;
        try {
            Process p = Runtime.getRuntime().exec(batFile, null, new File(dir));
            in = p.getInputStream();
            isr = new InputStreamReader(in, "GBK");
            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                LOGGER.info(line);
                if (finishCheck.isFinish(line)) {
                    break;
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // ignored
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // ignored
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    // ignored
                }
            }
        }
    }
}
