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

    }
}
