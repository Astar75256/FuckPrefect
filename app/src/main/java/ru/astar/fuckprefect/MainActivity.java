package ru.astar.fuckprefect;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ImageView previewImage;
    private Button prevButton;
    private Button nextButton;
    private Button saveButton;
    private Button decrementMinuteButton;
    private Button incrementMinuteButton;
    private Button setDateButton;
    private TextView settedDateTextView;
    private TextView countImageTextView;

    private Photo photo;
    private Calendar calendar;
    private String dateTimeString;
    private String[] listFiles;
    private int itemImage = 0;
    private SharedPreferences options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Tools.init(this);
        photo = new Photo();
        calendar = Calendar.getInstance();

        setDateTimeString();
        initView();
    }

    private void initView() {
        previewImage = (ImageView) findViewById(R.id.previewImage);
        prevButton = (Button) findViewById(R.id.prevImageButton);
        nextButton = (Button) findViewById(R.id.nextImageButton);
        saveButton = (Button) findViewById(R.id.saveImageButton);
        decrementMinuteButton = (Button) findViewById(R.id.decrementMinuteButton);
        incrementMinuteButton = (Button) findViewById(R.id.incrementMinuteButton);
        setDateButton = (Button) findViewById(R.id.setDateTimeButton);
        settedDateTextView = (TextView) findViewById(R.id.settedDataTextView);
        countImageTextView = (TextView) findViewById(R.id.countImageTextView);


        ClickListener clickListener = new ClickListener();

        previewImage.setOnClickListener(clickListener);
        prevButton.setOnClickListener(clickListener);
        nextButton.setOnClickListener(clickListener);
        saveButton.setOnClickListener(clickListener);
        decrementMinuteButton.setOnClickListener(clickListener);
        incrementMinuteButton.setOnClickListener(clickListener);
        setDateButton.setOnClickListener(clickListener);

        if (listFiles == null)
            countImageTextView.setText("Откройте директорию (кнопка вверху)");

        // обновить дату в TextView
        updateDateTextView();
        // перезагрузить настройки
        reloadSettings();
    }

    /**
     * Перезагрузить настройки
     */
    private void reloadSettings() {
        if (options == null) options = getSharedPreferences(Tools.PREF_NAME, MODE_PRIVATE);

        // цвет
        int colorRed   = options.contains(Tools.PREF_RED_COLOR) ? options.getInt(Tools.PREF_RED_COLOR, 0) : Photo.DEFAULT_COLOR[0];
        int colorGreen = options.contains(Tools.PREF_GREEN_COLOR) ? options.getInt(Tools.PREF_GREEN_COLOR, 0) : Photo.DEFAULT_COLOR[1];
        int colorBlue  = options.contains(Tools.PREF_BLUE_COLOR) ? options.getInt(Tools.PREF_BLUE_COLOR, 0) : Photo.DEFAULT_COLOR[2];

        // позиция текста
        int xPos = options.contains(Tools.PREF_X_POS) ? options.getInt(Tools.PREF_X_POS, 0) : Photo.DEFAULT_X_POS;
        int yPos = options.contains(Tools.PREF_Y_POS) ? options.getInt(Tools.PREF_Y_POS, 0) : Photo.DEFAULT_Y_POS;

        // размер шрифта
        int textSize = options.contains(Tools.PREF_TEXT_SIZE) ? options.getInt(Tools.PREF_TEXT_SIZE, 0) : Photo.DEFAULT_FONT_SIZE;

        // качество сохраняемой картинки
        int quality = options.contains(Tools.PREF_QUALITY) ? options.getInt(Tools.PREF_QUALITY, 0) : Photo.DEFAULT_QUALITY;

        photo.setColor(new int[] {colorRed, colorGreen, colorBlue});
        photo.setxPos(xPos);
        photo.setyPos(yPos);
        photo.setQuality(quality);
        photo.setFontSize(textSize);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SettingActivity.REQ_CODE_SETTINGS) {
            if (resultCode == RESULT_OK) {
                reloadSettings();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.openDirectoryItem:
                openDir();
                break;

            case R.id.settingItem:
                SettingActivity.startActivity(this, SettingActivity.REQ_CODE_SETTINGS);
                break;

            case R.id.aboutItem:
                showAbout();
                break;

            case R.id.exitItem:
                finish();
                break;
        }

        return true;
    }


    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.previewImage:
                    reloadImage();
                    break;

                case R.id.prevImageButton:
                    chooseImage(Tools.Mode.LEFT);
                    break;

                case R.id.nextImageButton:
                    chooseImage(Tools.Mode.RIGHT);
                    break;

                case R.id.saveImageButton:
                    saveAndEditImage();
                    break;

                case R.id.setDateTimeButton:
                    showDateDialog();
                    showTimeDialog();
                    break;

                case R.id.decrementMinuteButton:
                    setMinute(Tools.Mode.LEFT);
                    break;

                case R.id.incrementMinuteButton:
                    setMinute(Tools.Mode.RIGHT);
                    break;
            }
        }
    }

    /**
     * Установить минуты
     * @param mode LEFT меньше, RIGHT больше на одну минуту
     */
    private void setMinute(Tools.Mode mode) {
        int minute = calendar.get(Calendar.MINUTE);

        switch (mode) {
            case LEFT:
                minute--;
                break;
            case RIGHT:
                minute++;
                break;
        }

        calendar.set(Calendar.MINUTE, minute);
        updateDateTextView();
    }

    /**
     * Показать алерт
     */
    private void showAbout() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("О программе")
                .setMessage("Программа для вставки произвольной даты и времени в фотографии. " +
                "Писалась для личных нужд для работы.\n\n" +
                "Автор: Astar75, 2017 год. (с)")
                .setCancelable(false)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Показать диалог выбора даты
     */
    private void showDateDialog() {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateTextView();
            }
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    /**
     * Показать диалог выбора времени
     */
    private void showTimeDialog() {
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                updateDateTextView();
            }
        },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true);
        dialog.show();
    }

    /**
     * Обновить дату на экране
     */
    private void updateDateTextView() {
        setDateTimeString();
        settedDateTextView.setText(getString(R.string.setted_data) + " " + dateTimeString);
    }

    /**
     * установить дату и время в строку
     */
    private void setDateTimeString() {
        dateTimeString = Tools.getStringDate(calendar.getTime());
    }

    /**
     * Перезагрузить изображение
     */
    private void reloadImage() {
        if (listFiles != null && listFiles.length != 0) {
            setPreviewImage(listFiles[itemImage]);
        }
    }

    /**
     * Сохранить и отредактировать картинку
     */
    private void saveAndEditImage() {
        if (listFiles != null && listFiles.length != 0) {
            String filename = photo.getFilename();
            filename = filename.replace("IMG_", "photo_");
            if (!filename.isEmpty()) {
                filename = Tools.DIR_OUT + filename;  // имя файла
                photo.setText(dateTimeString);        // задаем дату в качестве текста для вывода
                photo.edit();                         // редактируем картинку
                previewImage.setImageBitmap(photo.getCurrentImage());
                if (photo.save(filename)) {           // сохраняем в файловой системе
                    Tools.showMessage("Фото сохранено " + filename);
                    return;
                }
            }
        }
        Tools.showMessage("Ошибка при сохранении!");
    }

    /**
     * Открыть директорию source
     */
    private void openDir() {
        listFiles = Tools.getListFiles();
        if (listFiles != null && listFiles.length != 0) {
            // выводим количество фото
            countImageTextView
                    .setText(getString(R.string.index_photo) + (itemImage + 1) + "; "
                            + getString(R.string.count_photo) + listFiles.length);
            // устанавливаем Первое фото
            setPreviewImage(listFiles[0]);
            return;
        }
        Tools.showMessage("Папка пуста!");
    }

    /**
     * Выбрать картинку (листать влево или вправо)
     *
     * @param mode режим: LEFT (влево) и RIGHT (вправо)
     */
    private void chooseImage(Tools.Mode mode) {
        if (listFiles != null && listFiles.length != 0) {
            switch (mode) {
                case LEFT:
                    itemImage--;
                    break;

                case RIGHT:
                    itemImage++;
                    break;
            }
            if (itemImage < 0) {
                itemImage = 0;
                Tools.showMessage("Это первая картинка!");
                return;
            }
            if (itemImage > listFiles.length - 1) {
                itemImage = listFiles.length - 1;
                Tools.showMessage("Это последняя картинка!");
                return;
            }

            countImageTextView
                    .setText(getString(R.string.index_photo) + (itemImage + 1) + "; "
                            + getString(R.string.count_photo) + listFiles.length);
            setPreviewImage(listFiles[itemImage]);
        }
    }

    /**
     * Установить картинку в ImageView
     *
     * @param filename путь к картинке
     */
    private void setPreviewImage(String filename) {
        if (!filename.isEmpty()) {
            previewImage.setBackground(null);
            previewImage.setImageBitmap(photo.open(filename));
        }
    }
}
