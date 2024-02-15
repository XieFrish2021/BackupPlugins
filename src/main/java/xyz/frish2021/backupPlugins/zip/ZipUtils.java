package xyz.frish2021.backupPlugins.zip;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {
    public static void zipFiles(String source, String dest) throws IOException {
        File file = new File(source);
        ZipOutputStream zipOutputStream = null;
        FileOutputStream fileOutputStream = null;

        fileOutputStream = new FileOutputStream(dest);
        zipOutputStream = new ZipOutputStream(fileOutputStream);
        if (file.isDirectory()) {
            directory(zipOutputStream, file, "");
        }

        if (zipOutputStream != null) {
            zipOutputStream.close();
        }
        if (fileOutputStream != null) {
            fileOutputStream.close();
        }
    }

    public static boolean unZipFiles(String zipFileName, String descFileName) {
        String descFileNames = descFileName;
        if (!descFileNames.endsWith(File.separator)) {
            descFileNames = descFileNames + File.separator;
        }
        try {
            ZipFile zipFile = new ZipFile(zipFileName);
            ZipArchiveEntry entry = null;
            String entryName = null;
            String descFileDir = null;
            byte[] buf = new byte[4096];
            int readByte = 0;
            @SuppressWarnings("rawtypes")
            Enumeration enums = zipFile.getEntries();
            while (enums.hasMoreElements()) {
                entry = (ZipArchiveEntry) enums.nextElement();
                entryName = entry.getName();
                descFileDir = descFileNames + entryName;
                if (entry.isDirectory()) {
                    new File(descFileDir).mkdirs();
                    continue;
                } else {
                    new File(descFileDir).getParentFile().mkdirs();
                }
                File file = new File(descFileDir);
                OutputStream os = new FileOutputStream(file);
                InputStream is = zipFile.getInputStream(entry);
                while ((readByte = is.read(buf)) != -1) {
                    os.write(buf, 0, readByte);
                }
                os.close();
                is.close();
            }
            zipFile.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static void directory(ZipOutputStream zipOutputStream, File file, String parentFileName) {
        File[] files = file.listFiles();
        String parentFileNameTemp = null;
        for (File fileTemp : files) {
            parentFileNameTemp = StringUtils.isEmpty(parentFileName) ? fileTemp.getName() : parentFileName + "/" + fileTemp.getName();
            if (fileTemp.isDirectory()) {
                directory(zipOutputStream, fileTemp, parentFileNameTemp);
            } else {
                zipFile(zipOutputStream, fileTemp, parentFileNameTemp);
            }
        }
    }

    private static void zipFile(ZipOutputStream zipOutputStream, File file, String parentFileName) {
        FileInputStream in = null;
        try {
            ZipEntry zipEntry = new ZipEntry(parentFileName);
            zipOutputStream.putNextEntry(zipEntry);
            in = new FileInputStream(file);
            int len;
            byte[] buf = new byte[8 * 1024];
            while ((len = in.read(buf)) != -1) {
                zipOutputStream.write(buf, 0, len);
            }
            zipOutputStream.closeEntry();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
