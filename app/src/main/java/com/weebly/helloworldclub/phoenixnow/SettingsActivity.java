package com.weebly.helloworldclub.phoenixnow;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    TextView tx;
    Typeface custom_font;
    @Override
    protected void onCreate(Bundle savedinstancestance){
        super.onCreate(savedinstancestance);
        setContentView(R.layout.settings);
        custom_font = Typeface.createFromAsset(getAssets(), "fonts/CinzelDecorative.ttf");
        tx = (TextView) findViewById(R.id.title);
        tx.setTypeface(custom_font);
        File settings=new File(getApplicationContext().getFilesDir().getPath()+"/settings.txt");
            try {
                FileInputStream fis = new FileInputStream(settings);
                byte[] buffer=new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                String string=new String(buffer,"UTF8");
                //set switches on or off based on memory here
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch(java.io.IOException e){
                e.printStackTrace();
            }
        //add listeners to handle switches
    }
    public void initializeSettings(Context context){
        File settings=new File(context.getApplicationContext().getFilesDir().getPath()+"/settings.txt");
        if(!settings.exists()) {
            try {
                settings.createNewFile();
//                FileOutputStream fos=new FileOutputStream(settings);
//                String string="rememberLogin:off ";//initialize switches
//                fos.write(string.getBytes());
//                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
