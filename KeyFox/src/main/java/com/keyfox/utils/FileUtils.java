package com.keyfox.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

/**
 * 获取当前文件夹下所有文件名称
 * Created by Administrator on 2017/12/16 0016.
 */
public class FileUtils {

    public static File[] getFileName(String path) {
        File f = new File(path);
        if (!f.exists()) {
            System.out.println(path + " not exists");
            return null;
        }
        return f.listFiles();

    }

    // 读取文件指定行。
    public static String readAppointedLineNumber(File sourceFile, int lineNumber) {
        FileReader in;
        try {
            in = new FileReader(sourceFile);
            LineNumberReader reader = new LineNumberReader(in);
            if (lineNumber < 0 || lineNumber > getTotalLines(sourceFile)) {
                return null;
            }
            int lines = 0;
            String s = "";
            while (s != null) {
                lines++;
                s = reader.readLine();
                if ((lines - lineNumber) == 0) {
                   // System.exit(0);
                    return s;
                }
            }
            reader.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 文件内容的总行数。
    public static int getTotalLines(File file) throws IOException {
        FileReader in = new FileReader(file);
        LineNumberReader reader = new LineNumberReader(in);
        String s = reader.readLine();
        int lines = 0;
        while (s != null) {
            lines++;
            s = reader.readLine();
        }
        reader.close();
        in.close();
        return lines;
    }

    public static void main(String[] args) {
        int lineNumber = 2;
        // 读取文件
        File sourceFile = new
                File("E:\\workspace\\Total\\2017-12-10_00-03-53_1511856213.json");

        // 读取指定的行
        String s = readAppointedLineNumber(sourceFile, lineNumber);
        // 获取文件的内容的总行数
        System.out.println(s);

    }
}
