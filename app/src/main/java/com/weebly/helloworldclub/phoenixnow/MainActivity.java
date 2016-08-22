package com.weebly.helloworldclub.phoenixnow;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
    LocationManager lm;
    LocationListener mLocationListener=new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };
    private static MainActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        SettingsActivity settings=new SettingsActivity();
        settings.initializeSettings(getApplicationContext());
        Memory memory=new Memory();
        lm=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!memory.loggedIn()) {
            setContentView(R.layout.titlepage);
        } else {
            setContentView(R.layout.homepage);
            Log.d("MainActivityToken", memory.getToken());
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mLocationListener);
            TextView welcometext=(TextView)findViewById(R.id.signintext);
            welcometext.setText("Welcome, " + memory.getEmail()+"!");
        }
    }
    public static MainActivity getActivity(){
        return activity;
    }
    @Override
    protected void onStop(){
        super.onStop();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lm.removeUpdates(mLocationListener);
    }
    @Override
    protected void onResume(){
        super.onResume();
        Memory memory=new Memory();
        lm=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!memory.loggedIn()) {
            setContentView(R.layout.titlepage);
        } else {
            setContentView(R.layout.homepage);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mLocationListener);
            TextView welcometext=(TextView)findViewById(R.id.signintext);
            welcometext.setText("Welcome, " + memory.getEmail()+"!");
        }
    }
    public void loginActivity(View view) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
    public void registerActivity(View view) {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
    public void signIn(View view) {
        if(Settings.Secure.getString(this.getApplicationContext().getContentResolver(),Settings.Secure.ALLOW_MOCK_LOCATION).equals("1")){
            Toast.makeText(getApplicationContext(),"Please turn off location spoofing",Toast.LENGTH_LONG).show();
        }else {
            MyThread thread=new MyThread(activity);
            thread.start();
        }
    }
    public class MyThread extends Thread{
        private MainActivity activity;
        public MyThread(MainActivity activity){
            this.activity = activity;
        }
        @Override
        public void run(){
            BackEnd backend = new BackEnd();
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            boolean gps_enabled = false;
            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ex) {
            }
            if (gps_enabled) {
                makeToast("Signing in...");
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Long start = System.currentTimeMillis();
                boolean searching = true;
                boolean found = true;
                while (location == null || Math.abs(location.getTime() - System.currentTimeMillis()) > 1000 && searching) {
                    location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (System.currentTimeMillis() - start > 5000) {
                        searching = false;
                        found = false;
                    }
                }
                if (found) {
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();
                    backend.signIn(latitude, longitude, new BackEnd.BackEndListener() {
                        @Override
                        public void onSuccess(String data) {
                            makeToast("Signed in!");
                        }

                        @Override
                        public void onFailure(String message) {
                            try {
                                JSONObject json=new JSONObject(message);
                                setText(json.getString("message"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled() {
                            makeToast("Server request timed out");
                        }
                    });
                } else {
                    setText("Couldn't resolve location in 5 seconds: try again");
                }
            } else {
                makeToast("Please enable location");
            }
        }
    }
    public void makeToast(final String message){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(),message,Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void setText(String message){

        final String m=message;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
               Toast.makeText(activity.getApplicationContext(), m, Toast.LENGTH_LONG).show();
            }
        });
    }
    public void signOut(View view){
        Memory memory=new Memory();
        memory.logoutUser();
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
    }
    public void openSettings(View view){
        Intent intent=new Intent(MainActivity.this,SettingsActivity.class);
        startActivity(intent);
    }
    public void openSchedule(View view){
        Intent intent=new Intent(MainActivity.this,ScheduleActivity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed(){

    }
}
