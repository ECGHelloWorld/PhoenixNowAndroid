package com.weebly.helloworldclub.phoenixnow;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public MainActivity(){

    }
    private LocationManager lm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BackEnd backend = new BackEnd();
        lm=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (backend.getToken() == null) {
            setContentView(R.layout.titlepage);
        } else if (backend.getToken() != null) {
            setContentView(R.layout.homepage);
            Log.d("MainActivityToken", backend.getToken());
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
        BackEnd backend = new BackEnd();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location!=null){
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            backend.signIn(latitude,longitude,this);
        }else{
            Toast.makeText(getApplicationContext(),"Please enable location",Toast.LENGTH_LONG).show();
        }
    }
}
