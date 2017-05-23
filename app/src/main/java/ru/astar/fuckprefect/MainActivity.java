package ru.astar.fuckprefect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
    private String[] listFiles;
    private int itemImage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        Tools.init(this);
        photo = new Photo();
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
                case R.id.prevImageButton:
                    chooseImage(Tools.Mode.LEFT);
                    break;

                case R.id.nextImageButton:
                    chooseImage(Tools.Mode.RIGHT);
                    break;

                case R.id.saveImageButton:
                    saveAndEditImage();
                    break;
            }
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
                    .setText(getString(R.string.index_photo) + (itemImage + 1) +  "; "
                            + getString(R.string.count_photo) + listFiles.length);
            // устанавливаем Первое фото
            setPreviewImage(listFiles[0]);
            return;
        }
        Tools.showMessage("Папка пуста!");
    }

    /**
     * Выбрать картинку (листать влево или вправо)
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
                    .setText(getString(R.string.index_photo) + (itemImage + 1) +  "; "
                            + getString(R.string.count_photo) + listFiles.length);
            setPreviewImage(listFiles[itemImage]);
        }
    }

    /**
     * Установить картинку в ImageView
     * @param filename
     */
    private void setPreviewImage(String filename) {
        if (!filename.isEmpty()) {
            previewImage.setImageBitmap(photo.open(filename));
        }
    }
}
