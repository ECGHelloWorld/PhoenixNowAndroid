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
        Switch rememberLogin=(Switch)findViewById(R.id.rememberLogin);
            try {
                FileInputStream fis = new FileInputStream(settings);
                byte[] buffer=new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                String string=new String(buffer,"UTF8");
                if(string.contains("rememberLogin:on")){
                    rememberLogin.setChecked(true);
                }else{
                    rememberLogin.setChecked(false);
                }
                //set switches on or off based on memory here
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch(java.io.IOException e){
                e.printStackTrace();
            }
        rememberLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                try {
                    File settings = new File(getApplicationContext().getFilesDir().getPath() + "/settings.txt");
                    FileInputStream fis = new FileInputStream(settings);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    fis.close();
                    String string = new String(buffer, "UTF8");
                    Log.d("settings initial", string);
                    if (string.contains("rememberLogin:on")) {
                        string = string.replace("rememberLogin:on", "rememberLogin:off");
                    } else {
                        string = string.replace("rememberLogin:off", "rememberLogin:on");
                    }
                    Log.d("settings final", string);
                    FileOutputStream fos = new FileOutputStream(settings);
                    fos.write(string.getBytes());
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
        });
        //add more listeners with same format as above for other switches
    }
    public boolean getRememberLogin(Context context){
        File settings=new File(context.getApplicationContext().getFilesDir().getPath()+"/settings.txt");
        if(settings.exists()) {
            try {
                FileInputStream fis = new FileInputStream(settings);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                String string = new String(buffer, "UTF8");
                if (string.contains("rememberLogin:on")) {
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
                Switch switch1=(Switch)findViewById(R.id.rememberLogin);
                switch1.setChecked(false);//initialize switches here
                settings.createNewFile();
                FileOutputStream fos=new FileOutputStream(settings);
                String string="rememberLogin:off ";//and here
                fos.write(string.getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
