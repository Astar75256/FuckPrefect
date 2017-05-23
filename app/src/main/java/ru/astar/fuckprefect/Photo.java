package ru.astar.fuckprefect;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by molot on 23.05.2017.
 */

public class Photo {

    public static final String TAG = "Class Photo: ";
    public static final int DEFAULT_QUALITY = 75;
    public static final int DEFAULT_FONT_SIZE = 145;
    public static final int DEFAULT_X_POS = 1000;
    public static final int DEFAULT_Y_POS = 1000;
    public static final int[] DEFAULT_COLOR = { 200, 140, 0 };

    private Bitmap currentImage = null;
    private int quality;
    private int fontSize;
    private int xPos;
    private int yPos;
    private int[] color = new int[3];
    private String text = null;

    public Bitmap open(String filename) {
        if (!filename.isEmpty())
            return currentImage = BitmapFactory.decodeFile(filename);

        return null;
    }

    public boolean save(String filename) {
        if (currentImage != null && !filename.isEmpty()) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(filename);
                if (quality <= 0) quality = DEFAULT_QUALITY;
                currentImage.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                return true;
            } catch (IOException e) {
                Log.d(TAG, "Ошибка сохранения! " + e.getMessage());
                return false;
            }
        }
        return false;
    }

    public Bitmap edit() {
        if (currentImage != null && !text.isEmpty()) {
            if (fontSize <= 0) fontSize = DEFAULT_FONT_SIZE;
            if (xPos <= 0) xPos = DEFAULT_X_POS;
            if (yPos <= 0) yPos = DEFAULT_Y_POS;
            if (color == null) color = DEFAULT_COLOR;
            if (color[0] <= 0 && color[1] <=0 && color[2] <= 0) color = DEFAULT_COLOR;

            Bitmap.Config config = currentImage.getConfig();
            if (config == null) config = Bitmap.Config.ARGB_8888;
            currentImage = currentImage.copy(config, true);
            Canvas canvas = new Canvas(currentImage);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.rgb(color[0], color[1], color[2]));
            paint.setTextSize(fontSize);
            canvas.drawText(text, xPos, yPos, paint);

            return currentImage;
        }
        return null;

    }

    public void close() {
        currentImage = null;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public int[] getColor() {
        return color;
    }

    public void setColor(int[] color) {
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public int getQuality() {
        return quality;
    }

    public Bitmap getCurrentImage() {
        return currentImage;
    }

    public void setCurrentImage(Bitmap image) {
        this.currentImage = image;
    }
}
