package ykooze.ayaseruri.codesslib.io;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.os.Environment;

/**
 * Created by wufeiyang on 16/6/18.
 */
public class FileUtils {
    /**
     * 读取文件为字符串
     * @param file 文件
     * @return 文件内容字符串
     * @throws IOException
     */
    public static String readStringFile(File file) throws IOException {
        String text = null;
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            text = readStringInput(is);
        } finally {
            if (is != null) {
                try {
                    is.close();
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        }
        return text;
    }

    /**
     * 读取输入流为字符串,最常见的是网络请求
     * @param is 输入流
     * @return 输入流内容字符串
     * @throws IOException
     */
    public static String readStringInput(InputStream is) throws IOException {
        StringBuilder strBuilder = new StringBuilder();
        String line;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is));
            while ((line = reader.readLine()) != null) {
                strBuilder.append(line).append("\r\n");
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return strBuilder.toString();
    }

    /**
     * 把字符串写入到文件中
     * @param file 被写入的目标文件
     * @param str 要写入的字符串内容
     * @throws IOException
     */
    public static void writeStringFile(File file, String str) throws IOException {
        DataOutputStream out = null;
        try {
            out = new DataOutputStream(new FileOutputStream(file));
            out.write(str.getBytes());
        } finally {
            if (out != null) {
                try {
                    out.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 解压zip文件
     * @param zipFilePath
     * @param destPath
     */
    public static void unzipFile(String zipFilePath, String destPath) throws IOException {
        // check or create dest folder
        File destFile = new File(destPath);
        if (!destFile.exists()) {
            destFile.mkdirs();
        }

        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry zipEntry;
        String zipEntryName;
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            zipEntryName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                File folder = new File(destPath + File.separator + zipEntryName);
                folder.mkdirs();
            } else {
                File file = new File(destPath + File.separator + zipEntryName);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                while ((len = zipInputStream.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }
        zipInputStream.close();
    }

    /**
     * 删除文件或者文件夹
     * @param directory
     */
    public static void delFile(File directory, boolean keepRoot) {
        if (directory != null && directory.exists()) {
            if (directory.isDirectory()) {
                for (File subDirectory : directory.listFiles()) {
                    delFile(subDirectory, false);
                }
            }

            if (!keepRoot) {
                directory.delete();
            }
        }
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 检测主外置存储是否可用
     * */
    public static boolean hasSDCardMounted() {
        String state = Environment.getExternalStorageState();
        if (state != null && state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    /**
     * 获取外置缓存File对象，如果系统本身方法返回为空，则自行拼凑路径
     * @param context
     * @return
     */

    public static File getExternalCacheDir(Context context) {
        File path = context.getExternalCacheDir();
        if (path != null) {
            return path;
        }
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    /**
     * 复制文件
     * @param orgPath 原始文件的地址
     * @param destination 目标地文件的地址
     * @throws IOException
     */

    public void copyFile(String orgPath, String destination) throws IOException {
        FileInputStream inputStream = new FileInputStream(orgPath);
        FileOutputStream outputStream = new FileOutputStream(destination);

        FileChannel ich = inputStream.getChannel();
        FileChannel och = outputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (true){
            if(-1 == ich.read(buffer)){
                break;
            }
            buffer.flip();
            och.write(buffer);
            buffer.clear();
        }

        ich.close();
        och.close();
        inputStream.close();
        outputStream.close();
    }
}
