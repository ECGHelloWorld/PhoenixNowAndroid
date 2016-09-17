package com.weebly.helloworldclub.phoenixnow;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
    Memory memory=new Memory();
    @Override
    protected void onCreate(Bundle savedinstancestance){
        super.onCreate(savedinstancestance);
        setContentView(R.layout.settings);
        custom_font = Typeface.createFromAsset(getAssets(), "fonts/CinzelDecorative.ttf");
        tx = (TextView) findViewById(R.id.title);
        tx.setTypeface(custom_font);
        //initialize switches
        checkinNotification=(Switch)findViewById(R.id.settings_checkinNotificationSwitch);
        checkinNotification.setChecked(memory.getCheckinNotification());
    }

    //activity paused
    public void onPause(){
        super.onPause();
        //commit switch status to memory
        if(checkinNotification.isChecked()){
            memory.setCheckinNotification(true);
        }else{
            memory.setCheckinNotification(false);
        }
    }
}
