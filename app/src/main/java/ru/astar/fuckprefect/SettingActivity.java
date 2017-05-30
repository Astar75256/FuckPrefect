package ru.astar.fuckprefect;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SettingActivity extends AppCompatActivity {

    public static final int REQ_CODE_SETTINGS = 100;

    private EditText xPosField;
    private EditText yPosField;
    private SeekBar redChannel;
    private SeekBar greenChannel;
    private SeekBar blueChannel;
    private TextView pallete;
    private TextView titleQuality;
    private SeekBar qualityBar;
    private EditText textSizeField;
    private Spinner fontTypeField;

    private SharedPreferences options;


    public static void startActivity(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, SettingActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
    }


    private void initView() {
        xPosField = (EditText) findViewById(R.id.posXField);
        yPosField = (EditText) findViewById(R.id.posYField);
        redChannel = (SeekBar) findViewById(R.id.redChannel);
        greenChannel = (SeekBar) findViewById(R.id.greenChannel);
        blueChannel = (SeekBar) findViewById(R.id.blueChannel);
        pallete = (TextView) findViewById(R.id.pallete);
        titleQuality = (TextView) findViewById(R.id.qualityTitle);
        qualityBar = (SeekBar) findViewById(R.id.quality);
        textSizeField = (EditText) findViewById(R.id.textSize);
        fontTypeField = (Spinner) findViewById(R.id.typeFont);

        SeekBarChangeListener changeListener = new SeekBarChangeListener();
        qualityBar.setOnSeekBarChangeListener(changeListener);
        redChannel.setOnSeekBarChangeListener(changeListener);
        greenChannel.setOnSeekBarChangeListener(changeListener);
        blueChannel.setOnSeekBarChangeListener(changeListener);

        if (options == null) options = getSharedPreferences(Tools.PREF_NAME, MODE_PRIVATE);

        // цвет
        Tools.colorRed   = options.contains(Tools.PREF_RED_COLOR) ? options.getInt(Tools.PREF_RED_COLOR, 0) : Photo.DEFAULT_COLOR[0];
        Tools.colorGreen = options.contains(Tools.PREF_GREEN_COLOR) ? options.getInt(Tools.PREF_GREEN_COLOR, 0) : Photo.DEFAULT_COLOR[1];
        Tools.colorBlue  = options.contains(Tools.PREF_BLUE_COLOR) ? options.getInt(Tools.PREF_BLUE_COLOR, 0) : Photo.DEFAULT_COLOR[2];

        // позиция текста
        Tools.xPos = options.contains(Tools.PREF_X_POS) ? options.getInt(Tools.PREF_X_POS, 0) : Photo.DEFAULT_X_POS;
        Tools.yPos = options.contains(Tools.PREF_Y_POS) ? options.getInt(Tools.PREF_Y_POS, 0) : Photo.DEFAULT_Y_POS;

        // размер шрифта
        Tools.textSize = options.contains(Tools.PREF_TEXT_SIZE) ? options.getInt(Tools.PREF_TEXT_SIZE, 0) : Photo.DEFAULT_FONT_SIZE;

        // качество сохраняемой картинки
        Tools.quality = options.contains(Tools.PREF_QUALITY) ? options.getInt(Tools.PREF_QUALITY, 0) : Photo.DEFAULT_QUALITY;

        xPosField.setText(String.valueOf(Tools.xPos));
        yPosField.setText(String.valueOf(Tools.yPos));
        redChannel.setProgress(Tools.colorRed);
        greenChannel.setProgress(Tools.colorGreen);
        blueChannel.setProgress(Tools.colorBlue);
        textSizeField.setText(String.valueOf(Tools.textSize));
        qualityBar.setProgress(Tools.quality);

        titleQuality.setText(getString(R.string.qulity_image) + " (" + qualityBar.getProgress() + ") %");
    }




    private class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int pos, boolean bool) {
            if (seekBar == qualityBar) {
                titleQuality.setText(getString(R.string.qulity_image) + " (" + pos + ") %");
            }


            if (seekBar == redChannel || seekBar == greenChannel || seekBar == blueChannel) {
                pallete.setText("Red: " + redChannel.getProgress()
                          + "; Green: " + greenChannel.getProgress()
                           + "; Blue: " + blueChannel.getProgress());
                pallete.setBackgroundColor(Color.rgb(
                        redChannel.getProgress(),
                        greenChannel.getProgress(),
                        blueChannel.getProgress()));
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveSettingsItem:
                save();
                break;

            case R.id.clearSourceItem:

                break;

            case R.id.clearOutItem:

                break;
        }
        return true;
    }

    private void save() {
        if (options != null) {
            SharedPreferences.Editor editor = options.edit();
            Tools.colorRed = redChannel.getProgress();
            Tools.colorGreen = greenChannel.getProgress();
            Tools.colorBlue = blueChannel.getProgress();
            Tools.quality = qualityBar.getProgress();
            Tools.xPos = Integer.parseInt(xPosField.getText().toString());
            Tools.yPos = Integer.parseInt(yPosField.getText().toString());
            Tools.textSize = Integer.parseInt(textSizeField.getText().toString());

            editor.putInt(Tools.PREF_RED_COLOR, Tools.colorRed);
            editor.putInt(Tools.PREF_GREEN_COLOR, Tools.colorGreen);
            editor.putInt(Tools.PREF_BLUE_COLOR, Tools.colorBlue);
            editor.putInt(Tools.PREF_QUALITY, Tools.quality);
            editor.putInt(Tools.PREF_X_POS, Tools.xPos);
            editor.putInt(Tools.PREF_Y_POS, Tools.yPos);
            editor.putInt(Tools.PREF_TEXT_SIZE, Tools.textSize);
            editor.commit();

            Tools.showMessage("Настройки сохранены!");
            setResult(RESULT_OK);
        }
    }
}
