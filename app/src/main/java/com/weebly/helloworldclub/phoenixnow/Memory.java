package com.weebly.helloworldclub.phoenixnow;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Created by Justin on 5/22/2016.
 */
public class Memory{
    public void loginUser(String email, String token){
        try {
            String filePath = MainActivity.getActivity().getFilesDir().getPath() + "/db.txt";
            File db=new File(filePath);
            db.createNewFile();
            FileOutputStream fos=new FileOutputStream(db);
            String string=email+" "+token;
            fos.write(string.getBytes("UTF8"));
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch(java.io.IOException e){
            e.printStackTrace();
        }
    }
    public void logoutUser(){
        String filePath = MainActivity.getActivity().getFilesDir().getPath() + "/db.txt";
        File db=new File(filePath);
        db.delete();
    }
    public boolean loggedIn(){
        String filePath = MainActivity.getActivity().getFilesDir().getPath() + "/db.txt";
        File db=new File(filePath);
        return db.exists();
//        return false;
    }

    public String getEmail(){
        try {
            String filePath = MainActivity.getActivity().getFilesDir().getPath() + "/db.txt";
            File db=new File(filePath);
            FileInputStream fis=new FileInputStream(db);
            byte[] buffer=new byte[fis.available()];
            fis.read(buffer);
            String string=new String(buffer,"UTF8");
            String email=string.substring(0,string.indexOf(" "));
            return email;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch(java.io.IOException e){
        }
        return null;
    }
    public String getToken(){
        try {
            String filePath = MainActivity.getActivity().getFilesDir().getPath() + "/db.txt";
            File db=new File(filePath);
            FileInputStream fis=new FileInputStream(db);
            byte[] buffer=new byte[fis.available()];
            fis.read(buffer);
            String string=new String(buffer,"UTF8");
            String token=string.substring(string.indexOf(" ")+1);
            return token;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch(java.io.IOException e){
        }
        return null;
    }
}
