package com.weebly.helloworldclub.phoenixnow;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Justin on 5/22/2016.
 */
public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedinstancestance){
        super.onCreate(savedinstancestance);
        setContentView(R.layout.settings);
        File settings=new File(getApplicationContext().getFilesDir().getPath()+"/settings.txt");
        Switch switch1=(Switch)findViewById(R.id.switch1);
            try {
                FileInputStream fis = new FileInputStream(settings);
                byte[] buffer=new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                String string=new String(buffer,"UTF8");
                if(string.contains("switch1:on")){
                    switch1.setChecked(true);
                }else{
                    switch1.setChecked(false);
                }
                //set switches on or off based on memory here
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch(java.io.IOException e){
                e.printStackTrace();
            }
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                try {
                    File settings=new File(getApplicationContext().getFilesDir().getPath()+"/settings.txt");
                    FileInputStream fis=new FileInputStream(settings);
                    byte[] buffer=new byte[fis.available()];
                    fis.read(buffer);
                    fis.close();
                    String string=new String(buffer,"UTF8");
                    Log.d("settings initial", string);
                    if(string.contains("switch1:on")) {
                        string=string.replace("switch1:on", "switch1:off");
                    }else{
                        string=string.replace("switch1:off", "switch1:on");
                    }
                    Log.d("settings final",string);
                    FileOutputStream fos=new FileOutputStream(settings);
                    fos.write(string.getBytes());
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }catch(java.io.IOException e){
                    e.printStackTrace();
                }
            }
        });
        //add more listeners with same format as above for other switches
    }
    public boolean getSwitch1(Context context){
        File settings=new File(context.getApplicationContext().getFilesDir().getPath()+"/settings.txt");
        if(settings.exists()) {
            try {
                FileInputStream fis = new FileInputStream(settings);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                String string = new String(buffer, "UTF8");
                if (string.contains("switch1:on")) {
                    return true;
                } else {
                    return false;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public void initializeSettings(Context context){
        File settings=new File(context.getApplicationContext().getFilesDir().getPath()+"/settings.txt");
        if(!settings.exists()) {
            try {
                Switch switch1=(Switch)findViewById(R.id.switch1);
                switch1.setChecked(false);//initialize switches here
                settings.createNewFile();
                FileOutputStream fos=new FileOutputStream(settings);
                String string="switch1:off ";//and here
                fos.write(string.getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
