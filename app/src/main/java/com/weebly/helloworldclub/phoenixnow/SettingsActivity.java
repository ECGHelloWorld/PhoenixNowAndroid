package com.weebly.helloworldclub.phoenixnow;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by Justin on 5/22/2016.
 */
public class SettingsActivity extends AppCompatActivity {
    /*
    This class handles the settings page
     */

    TextView tx;
    Typeface custom_font;
    Switch checkinNotification;
    Memory memory = new Memory();
    Toolbar toolbar;
    //    Button setReminder;
    int hour_x, minute_x;

    @Override
    protected void onCreate(Bundle savedinstancestance) {
        super.onCreate(savedinstancestance);
        setContentView(R.layout.settings);
        custom_font = Typeface.createFromAsset(getAssets(), "fonts/CinzelDecorative.ttf");
        tx = (TextView) findViewById(R.id.titletext);
        tx.setTypeface(custom_font);
//        setReminder = (Button) findViewById(R.id.setreminder_button);
        //initialize switches
        checkinNotification = (Switch) findViewById(R.id.settings_checkinNotificationSwitch);
        checkinNotification.setChecked(memory.getCheckinNotification());
        // initializing switch listener to activate and deactivate button
//        checkinNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                setReminder.setEnabled(isChecked);
//                if (!isChecked) {
//                    setReminder.setTextColor(Color.GRAY);
//                } else {
//                    setReminder.setTextColor(Color.WHITE);
//                }
//            }
//        });
//        setReminder.setEnabled(true);
//        // initializing reminder button listener
//        setReminder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog(0);
//            }
//        });

    }

    //creating dialog with default settings if the user has not selected any
    // and with the saved settings if the user has selected it
    protected Dialog onCreateDialog(int id) {
        if (id == 0) {
            if (memory.getNotificationMinute() == 0 && memory.getNotificationHour() == 2) {
                return new TimePickerDialog(SettingsActivity.this, timePickerListener, 12, 0, false);
            } else {
                return new TimePickerDialog(SettingsActivity.this, timePickerListener, memory.getNotificationHour(), memory.getNotificationMinute(), false);
            }
        }
        return null;
    }

    // Changing memory when user selects a new time
    protected TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hour_x = hourOfDay;
            minute_x = minute;
            memory.setNotificationTime(hour_x, minute_x);
            Log.d("Notification Time", String.valueOf(memory.getNotificationHour() + " " + memory.getNotificationMinute()));
            startNotificationService();
        }
    };

    //activity paused
    public void onPause() {
        super.onPause();
        //commit switch status to memory
        if (checkinNotification.isChecked()) {
            memory.setCheckinNotification(true);
        } else {
            memory.setCheckinNotification(false);
        }
    }

    public void onResume() {
        super.onResume();
        checkinNotification.setChecked(memory.getCheckinNotification());
        if (checkinNotification.isChecked()) {
//            setReminder.setEnabled(true);
//            setReminder.setTextColor(Color.WHITE);
        } else {
//            setReminder.setEnabled(false);
//            setReminder.setTextColor(Color.GRAY);
        }
    }

    public void startNotificationService() {
//        Calendar calendar = Calendar.getInstance();
//        Memory memory = new Memory();
//        calendar.set(Calendar.HOUR_OF_DAY, memory.getNotificationHour());
//        calendar.set(Calendar.MINUTE, memory.getNotificationMinute());
//
//        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
//
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//    }

    }
}
