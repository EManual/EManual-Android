package io.github.emanual.app.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class _ {
    /**
     * 判断是否是url
     *
     * @param link
     * @return
     */
    public static boolean isURL(String link) {
        return link.startsWith("http") || link.startsWith("https");
    }

    /**
     * 是否是数字
     *
     * @param s
     * @return
     */
    public static boolean isNumber(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 合并url
     *
     * @param baseUrl
     * @param strs
     * @return
     */
    public static String urlJoin(String baseUrl, String... strs) {
        StringBuilder sb = new StringBuilder(baseUrl);
        for (int i = 0; i < strs.length; i++) {
            try {
                sb.append("/").append(URLEncoder.encode(strs[i], "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 对url进行编码
     *
     * @param url
     * @return
     */
    public static String encodeURL(String url) {
        try {
            url = URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * 读文件
     */
    public static String readFile(String path) throws FileNotFoundException {
        return readFile(new File(path));
    }

    /**
     * 读文件
     */
    public static String readFile(File file) throws FileNotFoundException {
        return readFile(new FileInputStream(file));
    }

    /**
     * 读文件
     */
    public static String readFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();

        } catch (IOException e) {
        }
        return outputStream.toString();
    }

}
