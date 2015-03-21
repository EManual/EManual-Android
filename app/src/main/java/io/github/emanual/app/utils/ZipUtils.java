package io.github.emanual.app.utils;

import java.io.File;
import java.io.IOException;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

/**
 * Usage:
 * <code>
 * String path = "/Users/jayin/repos/FileManage/java.zip";
 * try {
 * ZipUtils.unZipFiles(path, "/Users/jayin/repos/FileManage/java");
 * } catch (IOException e) {
 * e.printStackTrace();
 * }
 * </code>
 */
public class ZipUtils {

    /**
     * 解压到指定目录
     *
     * @param zipPath
     * @param descDir
     */
    public static void unZipFiles(String zipPath, String descDir) throws IOException {
        unZipFiles(new File(zipPath), descDir);
    }

    /**
     * 解压文件到指定目录
     *
     * @param source
     * @param descDir
     */
    @SuppressWarnings("rawtypes")
    public static void unZipFiles(File source, String descDir) throws IOException {
        ZipFile zipFile;
        try {
            zipFile = new ZipFile(source);
            zipFile.extractAll(descDir);
        } catch (ZipException e) {
            e.printStackTrace();
        }

    }
}
