package ru.astar.fuckprefect;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity {

    private EditText xPosField;
    private EditText yPosField;
    private SeekBar redChannel;
    private SeekBar greenChannel;
    private SeekBar blueChannel;
    private TextView pallete;
    private SeekBar qualityBar;
    private EditText textSizeField;
    private Spinner fontTypeField;

    private void initView() {
        xPosField = (EditText) findViewById(R.id.posXField);
        yPosField = (EditText) findViewById(R.id.posYField);
        redChannel = (SeekBar) findViewById(R.id.redChannel);
        greenChannel = (SeekBar) findViewById(R.id.greenChannel);
        blueChannel = (SeekBar) findViewById(R.id.blueChannel);
        pallete = (TextView) findViewById(R.id.pallete);
        qualityBar = (SeekBar) findViewById(R.id.quality);
        textSizeField = (EditText) findViewById(R.id.textSize);
        fontTypeField = (Spinner) findViewById(R.id.typeFont);

        SharedPreferences preferences = Tools.getPreferences();

        if (preferences != null) {
            if (preferences.contains(Tools.PREF_X_POS))
                xPosField.setText(preferences.getInt(Tools.PREF_X_POS, 0));
            if (preferences.contains(Tools.PREF_Y_POS))
                yPosField.setText(preferences.getInt(Tools.PREF_Y_POS, 0));
            if (preferences.contains(Tools.PREF_RED_COLOR))
                redChannel.setProgress(preferences.getInt(Tools.PREF_RED_COLOR, 0));
            if (preferences.contains(Tools.PREF_GREEN_COLOR))
                greenChannel.setProgress(preferences.getInt(Tools.PREF_GREEN_COLOR, 0));
            if (preferences.contains(Tools.PREF_BLUE_COLOR))
                blueChannel.setProgress(preferences.getInt(Tools.PREF_BLUE_COLOR, 0));
            if (preferences.contains(Tools.PREF_QUALITY))
                qualityBar.setProgress(preferences.getInt(Tools.PREF_QUALITY, 0));
            if (preferences.contains(Tools.PREF_TEXT_SIZE))
                textSizeField.setText(preferences.getInt(Tools.PREF_TEXT_SIZE, 0));
            // if (preferences.contains(Tools.PREF_FONT_TYPE)) {}
        }
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, SettingActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
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
        SharedPreferences preferences = Tools.getPreferences();
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(Tools.PREF_X_POS, Integer.parseInt(xPosField.getText().toString()));
            editor.putInt(Tools.PREF_Y_POS, Integer.parseInt(yPosField.getText().toString()));
            editor.putInt(Tools.PREF_RED_COLOR, redChannel.getProgress());
            editor.putInt(Tools.PREF_GREEN_COLOR, greenChannel.getProgress());
            editor.putInt(Tools.PREF_BLUE_COLOR, blueChannel.getProgress());
            editor.putInt(Tools.PREF_QUALITY, qualityBar.getProgress());
            editor.putInt(Tools.PREF_TEXT_SIZE, Integer.parseInt(textSizeField.getText().toString()));
            // font
            editor.commit();
        }
    }
}
