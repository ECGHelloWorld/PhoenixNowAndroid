package com.weebly.helloworldclub.phoenixnow;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    /*
    This class handles the titlepage and homepage of the app, including signing in
     */


    private int notificationID = 0;//used for creating notifications

    //objects to handle gps services
    LocationManager lm;
    LocationListener mLocationListener = new LocationListener() {

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

    private static MainActivity activity;//used for getting instance of current activity
    TextView tx;//main TextView  in homepage layout
    private Toast toast;//class-wide toast for popup messages

    //activity lifecycle begins here
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
    }

    //static method for getting current instance of activity
    public static MainActivity getActivity() {
        return activity;
    }

    //default method for making notification with specified title and text body
    public void makeNotification(String title, String text, Boolean ifSuccessful) {
        Memory memory = new Memory();
        if (memory.getCheckinNotification()) {
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

    //activity has paused
    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //stop location updates
        lm.removeUpdates(mLocationListener);
    }

    //activity has resumed from pause
    @Override
    protected void onResume() {
        super.onResume();
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/CinzelDecorative.ttf");
        Memory memory = new Memory();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!memory.loggedIn()) {//if user has not already logged in, show the title page
            setContentView(R.layout.titlepage);
            tx = (TextView) findViewById(R.id.title);
            tx.setTypeface(custom_font);
        } else {//otherwise, show the home page
            setContentView(R.layout.homepage);
            tx = (TextView) findViewById(R.id.title);
            tx.setTypeface(custom_font);
            TextView welcometext = (TextView) findViewById(R.id.signintext);
            String text = "Welcome, " + memory.getEmail() + "!";
            welcometext.setText(text);
            getHistory();
            //check if user has permitted PhoenixNow to use location, and open a request dialog if not
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            }
            //if permission is enabled, begin querying location
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mLocationListener);

            if (memory.getNotificationHour() != 2 && memory.getNotificationMinute() != 0) {
                String weekDay;
                SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

                Calendar calendar = Calendar.getInstance();
                weekDay = dayFormat.format(calendar.getTime());
                startNotifications(weekDay);
            }
        }
    }

    //callback for dialog requesting permission for location
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
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

    //start loginactivity if user clicked login
    public void loginActivity(View view) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    //start registeractivity if user clicked register
    public void registerActivity(View view) {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    //user clicked sign in
    public void signIn(View view) {
        //reject if location spoofing is enabled
        if (Settings.Secure.getString(this.getApplicationContext().getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION).equals("1")) {
            Toast.makeText(getApplicationContext(), "Please turn off location spoofing", Toast.LENGTH_LONG).show();
        } else {//if location spoofing is off, begin a new sign in thread
            MyThread thread = new MyThread(activity);
            thread.start();
        }
    }

    //sign in thread
    public class MyThread extends Thread {
        private MainActivity activity;

        public MyThread(MainActivity activity) {
            this.activity = activity;
        }

        @Override
        public void run() {
            BackEnd backend = new BackEnd();//instantiate backend (server communication) object
            //check location permission yet again
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            }
            boolean gps_enabled = false;
            try {
                //determine if location is enabled
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ex) {
            }
            //if location is enabled, begin signing in
            if (gps_enabled) {
                makeToast("Signing in...please wait");
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Long start = System.currentTimeMillis();
                //query location until a location has been found within the last 1 second, or until 10 seconds elapses
                while (location == null || Math.abs(location.getTime() - System.currentTimeMillis()) > 1000) {
                    location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (System.currentTimeMillis() - start > 10000) {//10 seconds elapses, cancel signin and popup message
                        makeNotification("PhoenixNow", "Could not resolve location", false);
                        makeToast("Could not resolve location in 10 seconds: try again. If you are indoors please move outside to improve signal strength.");
                        return;
                    }
                }
                //location successfully determined
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                //use the backend object to sign in with latitude and longitude
                backend.signIn(latitude, longitude, new BackEnd.BackEndListener() {
                    @Override
                    public void onSuccess(String data) {
                        //signin successful, display message
                        makeNotification("PhoenixNow", "Successful Check-in!", true);
                        makeToast("You have successfully been checked-in");
                        getHistory();
                    }

                    @Override
                    public void onFailure(String message) {
                        //signin failed, display error message
                        try {
                            JSONObject json = new JSONObject(message);
                            makeNotification("PhoenixNow", "Unsuccessful Check-in, please try again.", false);
                            makeToast("Failed: " + json.getString("message"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled() {
                        //server timed out, display message
                        makeToast("Server request timed out");
                    }
                });
            } else {
                makeToast("Please enable location");
            }
        }
    }

    //default method for creating toasts
    public void makeToast(final String message) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    //user clicked sign out, erase memory and display title page
    public void signOut(View view) {
        Memory memory = new Memory();
        memory.logoutUser();
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
    }

    //user clicked settings, start settings activity
    public void openSettings(View view) {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    //user clicked schedule, start schedule activity
    public void openSchedule(View view) {
        Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
        startActivity(intent);
    }

    //user pressed history
    public void history(View view) {
        getHistory();
    }

    // Returns values of present for each day the user is here in the table
    public void getHistory() {
        BackEnd b = new BackEnd();
        b.getCheckins(new BackEnd.BackEndListener() {
            @Override
            public void onSuccess(String data) {
                try {
                    JSONObject json = new JSONObject(data);
                    if (json.getString("monday").equals("present")) {
                        TextView mondayCell = (TextView) findViewById(R.id.monday_history_cell);
                        setSignInText(mondayCell);
                    } else {

                    }
                    if (json.getString("tuesday").equals("present")) {
                        TextView tuesdayCell = (TextView) findViewById(R.id.tuesday_history_cell);
                        setSignInText(tuesdayCell);
                    } else {
                    }
                    if (json.getString("wednesday").equals("present")) {
                        TextView wednesdayCell = (TextView) findViewById(R.id.wednesday_history_cell);
                        setSignInText(wednesdayCell);
                    } else {
                    }
                    if (json.getString("thursday").equals("present")) {
                        TextView thursdayCell = (TextView) findViewById(R.id.thursday_history_cell);
                        setSignInText(thursdayCell);
                    } else {
                    }
                    if (json.getString("friday").equals("present")) {
                        TextView fridayCell = (TextView) findViewById(R.id.friday_history_cell);
                        setSignInText(fridayCell);
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message) {
            }

            @Override
            public void onCancelled() {

            }
        });
    }

    // Store the value of check-in for entered date
    public void startNotifications(final String today) {
        String weekDay;
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

        Calendar calendar = Calendar.getInstance();
        weekDay = dayFormat.format(calendar.getTime());

        if (weekDay.trim().toLowerCase() == "monday"
                || weekDay.trim().toLowerCase() == "tuesday"
                || weekDay.trim().toLowerCase() == "wednesday"
                || weekDay.trim().toLowerCase() == "thursday"
                || weekDay.trim().toLowerCase() == "friday") {
            BackEnd b = new BackEnd();
            b.getCheckins(new BackEnd.BackEndListener() {
                @Override
                public void onSuccess(String data) {
                    try {
                        JSONObject json = new JSONObject(data);
                        if (json.getString(today).equals("present")) {
                            new Memory().setCheckedInStatus(true);
                            Log.d("Notifications", "Notifications will appear");
                        } else {
                            new Memory().setCheckedInStatus(false);
                            Log.d("Notifications", "No notifications");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String message) {
                }

                @Override
                public void onCancelled() {

                }
            });
        } else {
            new Memory().setCheckedInStatus(true);
        }
        startNotificationService(true);
    }

    //default method for setting text
    public void setSignInText(final TextView view) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.setText("Present");
            }
        });
    }

    public void startNotificationService(Boolean isUserCheckedInToday) {
        Calendar calendar = Calendar.getInstance();
        Memory memory = new Memory();
        calendar.set(Calendar.HOUR_OF_DAY, memory.getNotificationHour());
        calendar.set(Calendar.MINUTE, memory.getNotificationMinute());

        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 21600000, pendingIntent);
    }

    //do nothing when back is pressed
    @Override
    public void onBackPressed() {

    }
}
