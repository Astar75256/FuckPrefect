package ru.astar.fuckprefect;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Tools.init(this);
        photo = new Photo();
        calendar = Calendar.getInstance();
        setDateTime();
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

        settedDateTextView.setText(getString(R.string.setted_data) + " " + dateTimeString);
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

                break;

            case R.id.aboutItem:

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
            }
        }
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
                setDateTime();
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
                setDateTime();
            }
        },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true);
        dialog.show();
    }

    private void setDateTime() {
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
            if (!filename.isEmpty()) {
                filename = Tools.DIR_OUT + filename;
                photo.setText("Какой то текст");
                photo.edit();
                previewImage.setImageBitmap(photo.getCurrentImage());
                if (photo.save(filename)) {
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
     * @param filename
     */
    private void setPreviewImage(String filename) {
        if (!filename.isEmpty()) {
            previewImage.setImageBitmap(photo.open(filename));
        }
    }
}
