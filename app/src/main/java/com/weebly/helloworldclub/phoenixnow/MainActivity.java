package com.weebly.helloworldclub.phoenixnow;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private int harambeClicks=0;
    private int notificationID=0;
    public void harambe(View view){
        harambeClicks++;
        if(harambeClicks>11){
            ImageView i=(ImageView)findViewById(R.id.logoimage);
            i.setImageResource(R.drawable.harambe);
        }
        if(harambeClicks>21){
            TextView t=(TextView)findViewById(R.id.title);
            t.setText("RIP Harambe 1999-2016");
        }
    }
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
    TextView tx;
    Typeface custom_font;
    private Toast toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        SettingsActivity settings=new SettingsActivity();
        custom_font = Typeface.createFromAsset(getAssets(), "fonts/CinzelDecorative.ttf");
        Memory memory=new Memory();
        lm=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!memory.loggedIn()) {
            setContentView(R.layout.titlepage);
            tx = (TextView) findViewById(R.id.title);
            tx.setTypeface(custom_font);
        } else {
            setContentView(R.layout.homepage);
            tx = (TextView) findViewById(R.id.title);
            tx.setTypeface(custom_font);
            TextView welcometext=(TextView)findViewById(R.id.signintext);
            String text="Welcome, " + memory.getEmail()+"!";
            welcometext.setText(text);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mLocationListener);
        }
    }


    public static MainActivity getActivity(){
        return activity;
    }

    public void makeNotification(String title, String text, Boolean ifSuccessful) {
        Memory memory=new Memory();
        if(memory.getCheckinNotification()) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(activity);
            builder.setContentTitle(title);
            builder.setContentText(text);
            builder.setTicker(title);
            builder.setSmallIcon(R.drawable.officiallogo);
            if (ifSuccessful) {
                builder.setVibrate(new long[]{0, 750, 100, 750});
                builder.setLights(Color.GREEN, 3000, 3000);
            } else if (!ifSuccessful) {
                builder.setVibrate(new long[]{0, 500});
                builder.setLights(Color.GREEN, 3000, 3000);
            }
            builder.setPriority(Notification.PRIORITY_MAX);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(notificationID, builder.build());
            notificationID++;
        }
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
        custom_font = Typeface.createFromAsset(getAssets(), "fonts/CinzelDecorative.ttf");
        if (!memory.loggedIn()) {
            setContentView(R.layout.titlepage);
            tx = (TextView) findViewById(R.id.title);
            tx.setTypeface(custom_font);
        } else {
            setContentView(R.layout.homepage);
            tx = (TextView) findViewById(R.id.title);
            tx.setTypeface(custom_font);
            TextView welcometext=(TextView)findViewById(R.id.signintext);
            String text="Welcome, " + memory.getEmail()+"!";
            welcometext.setText(text);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mLocationListener);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[],int[] grantResults){
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mLocationListener);
                } else {
                    makeToast("This app needs location permission to function");
                }
                return;
            }
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

    public void schedule(View view) {
        Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
        startActivity(intent);
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
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            }
            boolean gps_enabled = false;
            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ex) {
            }
            if (gps_enabled) {
                makeToast("Signing in...please wait");
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Long start = System.currentTimeMillis();
                while (location == null || Math.abs(location.getTime() - System.currentTimeMillis()) > 1000) {
                    location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (System.currentTimeMillis() - start > 10) {
                        makeNotification("PhoenixNow","Could not resolve location",false);
                        makeToast("Could not resolve location in 10 seconds: try again. If you are indoors please move outside to improve signal strength.");
                        return;
                    }
                }
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                backend.signIn(latitude, longitude, new BackEnd.BackEndListener() {
                    @Override
                    public void onSuccess(String data) {
                        makeNotification("PhoenixNow", "Successful Check-in!", true);
                        makeToast("You have successfully been checked-in");
                    }

                    @Override
                    public void onFailure(String message) {
                        try {
                            JSONObject json=new JSONObject(message);
                            makeNotification("PhoenixNow", "Unsuccessful Check-in, please try again.", false);
                            makeToast("Failed: "+json.getString("message"));
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
                makeToast("Please enable location");
            }
        }
    }
    public void makeToast(final String message){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(toast!=null){
                    toast.cancel();
                }
                toast=Toast.makeText(getBaseContext(),message,Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    public void setText(String message) {

        final String m = message;
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
