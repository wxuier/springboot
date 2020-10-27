package com.libra.mimipay.common;

import android.os.Environment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

public class SdcardFileUtils {
    private static final Logger logger = Logger.getLogger(SdcardFileUtils.class.getName());

    public static String read(String name) {
        File file = new File(Environment.getExternalStorageDirectory(), name);
        if (!file.exists()) {
            return null;
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String readLine = br.readLine();
            br.close();
            return readLine;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void write(String name, String content) {
        File file = new File(Environment.getExternalStorageDirectory(), name);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }
}
