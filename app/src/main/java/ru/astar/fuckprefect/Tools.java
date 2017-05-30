package ru.astar.fuckprefect;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
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

    public static final String PREF_NAME = "pref_fuck_prefect";

    public static final String FILE_FILTER_JPG = ".*\\.jpg";

    public static final String PREF_X_POS = "xPos";
    public static final String PREF_Y_POS = "yPos";
    public static final String PREF_RED_COLOR = "red";
    public static final String PREF_GREEN_COLOR = "green";
    public static final String PREF_BLUE_COLOR = "blue";
    public static final String PREF_QUALITY = "quality";
    public static final String PREF_TEXT_SIZE = "textSize";
    public static final String PREF_FONT_TYPE = "fontType";

    public static int colorRed;
    public static int colorGreen;
    public static int colorBlue;

    // позиция текста
    public static int xPos;
    public static int yPos;

    // размер шрифта
    public static int textSize;

    // качество сохраняемой картинки
    public static int quality;

    private static Context context;

    /**
     * Инициализация
     * @param context
     */
    public static void init(Context context) {
        Tools.context = context;
        Tools.createDirs();
    }

    /**
     * Возвращает дату и время в виде строки
     * @param date дата
     * @return вернет в формате 30.05.2017 18:30
     */
    public static String getStringDate(Date date) {
        return new SimpleDateFormat("dd.MM.yyyy HH:mm").format(date.getTime());
    }

    /**
     * Возвращает список файлов
     * @return
     */
    public static String[] getListFiles() {
        File dirSource = new File(DIR_SOURCE);
        if (dirSource.exists()) {
            File[] files = dirSource.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File file, String fileName) {
                    String filter = FILE_FILTER_JPG;  // файловый фильтр
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

    /**
     * Показать Toast сообщение
     * @param message текст сообщения
     */
    public static void showMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    /**
     * Создать папки Source и Out в файловой системе
     */
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

    public static void clearSourceDir() {
        File dirSource = new File(DIR_SOURCE);
        if (dirSource.exists() && dirSource.isDirectory()) {
            File[] files = dirSource.listFiles();
            for (File file :files) {
                file.delete();
            }
        }
    }

    public static void clearOutDir() {
        File dirSource = new File(DIR_OUT);
        if (dirSource.exists() && dirSource.isDirectory()) {
            File[] files = dirSource.listFiles();
            for (File file :files) {
                file.delete();
            }
        }
    }


}
