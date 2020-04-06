package com.mariiapasichna;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/*
1) Вывести список файлов заданной директории рекурсивно
2*) Скачать файл src.zip (программно), разархивировать его и вывести имена всех файлов в которых встречается строка
@FunctionalInterface
https://dl.dropboxusercontent.com/s/5y76skm8f5ged7j/src.zip
*/

public class Task1_2 {

    public static final String URL = "https://dl.dropboxusercontent.com/s/5y76skm8f5ged7j/src.zip";
    public static final String ZIP = "/Users/user/file.zip";
    public static final String DIRECTORY = "/Users/user/file";
    public static final String FIND = "@FunctionalInterface";
    public static final int BUFFER_SIZE = 4096;

    public static void main(String[] args) {
        loadZip();
        unzip();
        printFiles(DIRECTORY);
        System.out.println("================================================================");
        findFiles(DIRECTORY);
    }

    public static void findFiles(String directory) {
        File[] files = new File(directory).listFiles();
        for (File file : files) {
            if (file.isFile()) {
                try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
                    String currentLine;
                    while ((currentLine = bufferedReader.readLine()) != null) {
                        if (currentLine.contains(FIND)) {
                            System.out.println(file.getAbsolutePath());
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (file.isDirectory()) {
                findFiles(file.getAbsolutePath());
            }
        }
    }

    public static void printFiles(String directory) {
        File[] files = new File(directory).listFiles();
        for (File file : files) {
            if (file.isFile()) {
                System.out.println(file.getAbsolutePath());
            }
            if (file.isDirectory()) {
                printFiles(file.getAbsolutePath());
            }
        }
    }

    public static void unzip() {
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(ZIP))) {
            ZipEntry entry = zipInputStream.getNextEntry();
            while (entry != null) {
                String filePath = DIRECTORY + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    extractFile(zipInputStream, filePath);
                } else {
                    File file = new File(filePath);
                }
                zipInputStream.closeEntry();
                entry = zipInputStream.getNextEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void extractFile(ZipInputStream zipInputStream, String filePath) throws IOException {
        File parentPath = new File(filePath).getParentFile();
        if (!parentPath.exists() && !parentPath.mkdirs()) {
            throw new IOException("Could not create directory " + parentPath.getAbsolutePath());
        }
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytes = new byte[BUFFER_SIZE];
        int read;
        while ((read = zipInputStream.read(bytes)) != -1) {
            bufferedOutputStream.write(bytes, 0, read);
        }
        bufferedOutputStream.close();
    }

    public static void loadZip() {
        URL link = null;
        try {
            link = new URL(URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try (InputStream inputStream = new BufferedInputStream(link.openStream());
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             FileOutputStream fileOutputStream = new FileOutputStream(ZIP)) {
            byte[] bytes = new byte[BUFFER_SIZE];
            int n = inputStream.read(bytes);
            while (n != -1) {
                byteArrayOutputStream.write(bytes, 0, n);
                n = inputStream.read(bytes);
            }
            byte[] response = byteArrayOutputStream.toByteArray();
            fileOutputStream.write(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}