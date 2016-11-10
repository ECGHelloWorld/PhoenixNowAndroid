package com.weebly.helloworldclub.phoenixnow;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by nairv on 11/9/2016.
 */
public class HistoryActivity extends AppCompatActivity {

    int day, month_x, year_x;
    static final int DIALOG_ID = 0;

    TextView signInText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    String date;

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.history);
        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
        date = sdf.format(new Date(year_x - 1900, month_x, day));
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/CinzelDecorative.ttf");
        TextView title = (TextView) findViewById(R.id.title);
        title.setTypeface(custom_font);
        signInText = (TextView) findViewById(R.id.signintext);
        signInText.setText(signInText.getText() + date);
        getHistory();

    }


    public void getHistory() {
        BackEnd b = new BackEnd();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date(year_x, month_x, day));
        b.getCheckinHistory(new BackEnd.BackEndListener() {
            @Override
            public void onSuccess(String data) {
                try {
                    JSONObject json = new JSONObject(data);
                    Log.d("JSON", json.toString());
                    if (json.getString("monday").equals("present") || json.getString("monday").equals("")) {
                        TextView mondayCell = (TextView) findViewById(R.id.monday_history_cell);
                        setSignInText(mondayCell, json.getString("monday"));
                    } else {

                    }
                    if (json.getString("tuesday").equals("present") || json.getString("tuesday").equals("")) {
                        TextView tuesdayCell = (TextView) findViewById(R.id.tuesday_history_cell);
                        setSignInText(tuesdayCell, json.getString("tuesday"));
                    } else {
                    }
                    if (json.getString("wednesday").equals("present") || json.getString("wednesday").equals("")) {
                        TextView wednesdayCell = (TextView) findViewById(R.id.wednesday_history_cell);
                        setSignInText(wednesdayCell, json.getString("wednesday"));
                    } else {
                    }
                    if (json.getString("thursday").equals("present") || json.getString("thursday").equals("")) {
                        TextView thursdayCell = (TextView) findViewById(R.id.thursday_history_cell);
                        setSignInText(thursdayCell, json.getString("thursday"));
                    } else {
                    }
                    if (json.getString("friday").equals("present") || json.getString("friday").equals("")) {
                        TextView fridayCell = (TextView) findViewById(R.id.friday_history_cell);
                        setSignInText(fridayCell, json.getString("friday"));
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
        }, date);


    }

    public void setSignInText(final TextView view, final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.setText(text);
            }
        });
    }

    public void changeWeek(View view) {
        showDialog(DIALOG_ID);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID) {
            return new DatePickerDialog(this, dPickerListener, year_x, month_x, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dPickerListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    year_x = year;
                    month_x = monthOfYear;
                    day = dayOfMonth;
                    getHistory();
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
                    date = sdf.format(new Date(year_x - 1900, month_x, day));
                    signInText.setText("Viewing History for the Week of: \n \n" + date);
                }
            };
}
