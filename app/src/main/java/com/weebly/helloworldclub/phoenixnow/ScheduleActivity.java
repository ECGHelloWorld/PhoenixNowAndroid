package com.weebly.helloworldclub.phoenixnow;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Justin on 5/25/2016.
 */
public class ScheduleActivity extends AppCompatActivity {
    /*
    This class handles the form for submitting a schedule to the server
     */

    private Toast toast;
    TextView tx;
    Typeface custom_font;

    //activity created
    @Override
    public void onCreate(Bundle savedinstancestance){
        super.onCreate(savedinstancestance);
        setContentView(R.layout.schedule);
        custom_font = Typeface.createFromAsset(getAssets(), "fonts/CinzelDecorative.ttf");
        tx = (TextView) findViewById(R.id.title);
        tx.setTypeface(custom_font);
        BackEnd backend=new BackEnd();
        backend.getSchedule(new BackEnd.BackEndListener() {//retrieve schedule and set switches to correct position
            @Override
            public void onSuccess(String schedule) {
                try {
                    JSONObject json=new JSONObject(schedule);
                    setSwitches(json);
                    if(json.getBoolean("verified")) {
                        setText("Schedule has been approved");
                    }else{
                        setText("Schedule has not been approved");
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
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
    }

    //method for setting switches based on server feedback
    public void setSwitches(final JSONObject json){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Switch m=(Switch) findViewById(R.id.mswitch);
                    final Switch t=(Switch) findViewById(R.id.tswitch);
                    final Switch w=(Switch) findViewById(R.id.wswitch);
                    final Switch r=(Switch) findViewById(R.id.rswitch);
                    final Switch f=(Switch) findViewById(R.id.fswitch);
                    if (json.getString("schedule").contains("M")) {
                        m.setChecked(true);
                    } else {
                        m.setChecked(false);
                    }
                    if (json.getString("schedule").contains("T")) {
                        t.setChecked(true);
                    } else {
                        t.setChecked(false);
                    }
                    if (json.getString("schedule").contains("W")) {
                        w.setChecked(true);
                    } else {
                        w.setChecked(false);
                    }
                    if (json.getString("schedule").contains("R")) {
                        r.setChecked(true);
                    } else {
                        r.setChecked(false);
                    }
                    if (json.getString("schedule").contains("F")) {
                        f.setChecked(true);
                    } else {
                        f.setChecked(false);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    //method for setting text
    public void setText(final String message){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final TextView submitted=(TextView)findViewById(R.id.submittedschedule);
                submitted.setText(message);
            }
        });
    }

    //user pressed submit schedule, so send it to the server
    public void register(View view){
        Switch m=(Switch) findViewById(R.id.mswitch);
        Switch t=(Switch) findViewById(R.id.tswitch);
        Switch w=(Switch) findViewById(R.id.wswitch);
        Switch r=(Switch) findViewById(R.id.rswitch);
        Switch f=(Switch) findViewById(R.id.fswitch);
        BackEnd backend=new BackEnd(m,t,w,r,f);
        backend.sendSchedule(new BackEnd.BackEndListener() {//send schedule to the server
            @Override
            public void onSuccess(String data) {
                setText("Schedule sent for approval");
            }

            @Override
            public void onFailure(String message) {
                try{
                    JSONObject json=new JSONObject(message);
                    setText(json.getString("message"));
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled() {
                makeToast("Server request timed out");
            }
        });
    }

    //method for making toasts
    public void makeToast(final String message){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(toast!=null){
                    toast.cancel();
                }
                toast=Toast.makeText(getBaseContext(),message,Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
