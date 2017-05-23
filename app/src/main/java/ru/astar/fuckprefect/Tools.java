package ru.astar.fuckprefect;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by molot on 23.05.2017.
 */

public class Tools {

    enum Mode {
        LEFT,
        RIGHT
    }

    public static final String DIR_SOURCE = Environment.getExternalStorageDirectory() + "/FuckPrefect/source";
    public static final String DIR_OUT = Environment.getExternalStorageDirectory() + "/FuckPrefect/out";

    private static Context context;


    public static void init(Context context) {
        Tools.context = context;
        Tools.createDirs();
    }

    public static String getStringDate(Date date) {
        return new SimpleDateFormat("dd.MM.yyyy HH:mm").format(date.getTime());
    }

    public static String[] getListFiles() {
        File dirSource = new File(DIR_SOURCE);
        if (dirSource.exists()) {
            File[] files = dirSource.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File file, String fileName) {
                    String filter = ".*\\.jpg";
                    File tempFile = new File(String.format("%s/%s", file.getPath(), fileName));
                    if (tempFile.isFile())
                        return tempFile.getName().matches(filter);
                    return true;
                }
            });

            String[] filesToString = new String[files.length];

            for (int i = 0; i < files.length; i++)
                filesToString[i] = files[i].getAbsolutePath();

            return filesToString;
        }
        return null;
    }

    public static void showMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private static void createDirs() {
        File dirSource = new File(DIR_SOURCE);
        File dirOut = new File(DIR_OUT);
        if (!dirSource.exists()) {
            if (dirSource.mkdirs())
                showMessage("Директория " + dirSource.getName() + " создана.");
            else
                showMessage("Ошибка при создании директории " + dirSource.getName());
        }
        if (!dirOut.exists()) {
            if (dirOut.mkdirs())
                showMessage("Директория " + dirOut.getName() + " создана.");
            else
                showMessage("Ошибка при создании директории " + dirOut.getName());
        }
    }

}
