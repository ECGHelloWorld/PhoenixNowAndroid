package com.weebly.helloworldclub.phoenixnow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Justin on 5/25/2016.
 */
public class ScheduleActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedinstancestance){
        super.onCreate(savedinstancestance);
        setContentView(R.layout.schedule);
        TextView submitted=(TextView)findViewById(R.id.submittedschedule);
        TextView verified=(TextView)findViewById(R.id.verifiedschedule);
        Switch m=(Switch) findViewById(R.id.mswitch);
        Switch t=(Switch) findViewById(R.id.tswitch);
        Switch w=(Switch) findViewById(R.id.wswitch);
        Switch r=(Switch) findViewById(R.id.rswitch);
        Switch f=(Switch) findViewById(R.id.fswitch);
        BackEnd backend=new BackEnd();
        String schedule=backend.getSchedule(this);
        if(schedule.contains("Schedule")) {
            String submittedSchedule="";
            String verifiedSchedule="";
            try {
                JSONObject json=new JSONObject(schedule);
                verifiedSchedule=json.get("VerifiedSchedule").toString();
                submittedSchedule=json.get("SubmittedSchedule").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            submitted.setText("Submitted Schedule: "+submittedSchedule);
            verified.setText("Verified Schedule: "+verifiedSchedule);
            if (verifiedSchedule.contains("M")) {
                m.setChecked(true);
            } else {
                m.setChecked(false);
            }
            if (verifiedSchedule.contains("T")) {
                t.setChecked(true);
            } else {
                t.setChecked(false);
            }
            if (verifiedSchedule.contains("W")) {
                w.setChecked(true);
            } else {
                w.setChecked(false);
            }
            if (verifiedSchedule.contains("R")) {
                r.setChecked(true);
            } else {
                r.setChecked(false);
            }
            if (verifiedSchedule.contains("F")) {
                f.setChecked(true);
            } else {
                f.setChecked(false);
            }
        } else {
            submitted.setText("You haven't sent in a schedule yet");
        }
    }

    public void register(View view){
        Switch m=(Switch) findViewById(R.id.mswitch);
        Switch t=(Switch) findViewById(R.id.tswitch);
        Switch w=(Switch) findViewById(R.id.wswitch);
        Switch r=(Switch) findViewById(R.id.rswitch);
        Switch f=(Switch) findViewById(R.id.fswitch);
        BackEnd backend=new BackEnd(m,t,w,r,f);
        backend.sendSchedule(this);
    }
}
