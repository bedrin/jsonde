package com.jsonde.util.file;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String PATH_SEPARATOR = System.getProperty("path.separator");
    public static final String USER_HOME = System.getProperty("user.home");

    public static boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }

    public static boolean createFile(File file) {

        if (!file.exists()) {

            File directory = file.getParentFile();

            if (!directory.exists()) {
                return directory.mkdirs();
            } else {
                return true;
            }

        } else {
            return true;
        }

    }

    public static String canonizePath(String path) throws IOException {
        File file = new File(path);
        return file.getCanonicalPath();
    }

}
