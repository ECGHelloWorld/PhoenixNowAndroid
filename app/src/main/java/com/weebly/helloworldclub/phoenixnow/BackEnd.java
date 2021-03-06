package com.weebly.helloworldclub.phoenixnow;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;
import javax.security.cert.Certificate;

/**
 * Created by hunai on 3/4/2016.
 */
public class BackEnd {
    /*
    This class handles all server communication
     */

    //interface for receiving feedback from server
    public interface BackEndListener{
        public void onSuccess(String data);
        public void onFailure(String message);
        public void onCancelled();
    }

    //assorted variables do be defined later
    private EditText email;
    private EditText firstName;
    private EditText password;
    private EditText lastName;
    private EditText grade;
    private int code;
    private double latitude;
    private double longitude;
    private Switch monday;
    private Switch tuesday;
    private Switch wednesday;
    private Switch thursday;
    private Switch friday;

    //url for website api
    private static final String baseUrl="https://phoenixnow.org/api";


    //different constructor depending on information
    public BackEnd(EditText emailEditText, EditText passwordEditText,
                   EditText firstNameEditText,EditText lastNameEditText, EditText gradeEditText) {
        email=emailEditText;
        firstName=firstNameEditText;
        lastName=lastNameEditText;
        password=passwordEditText;
        grade=gradeEditText;
    }
    public BackEnd(Switch m,Switch t, Switch w, Switch r, Switch f){
        monday=m;
        tuesday=t;
        wednesday=w;
        thursday=r;
        friday=f;
    }

    public BackEnd(){

    }

    //register method
    public void register(BackEndListener listener) {
        RegisterThread thread = new RegisterThread(listener);
        thread.start();
    }
    //login method
    public void login(BackEndListener listener){
        LoginThread thread=new LoginThread(listener);
        thread.start();
    }
    //signin method
    public void signIn(double lat,double lon, BackEndListener listener){
        latitude=lat;
        longitude=lon;
        SigninThread thread=new SigninThread(listener);
        thread.start();
    }
    //method to send schedule to the server
    public void sendSchedule(BackEndListener listener){
        ScheduleThreadPost thread=new ScheduleThreadPost(listener);
        thread.start();
    }
    //method to retrieve schedule from the server
    public void getSchedule(BackEndListener listener){
        ScheduleThreadGet thread=new ScheduleThreadGet(listener);
        thread.start();
    }
    //method to retrieve checkin history from the server
    public void getCheckins(BackEndListener listener){
        CheckinThreadGet thread=new CheckinThreadGet(listener);
        thread.start();
    }

    //method to retrieve past checkin history from the server
    public void getCheckinHistory(BackEndListener listener, String date) {
        CheckinHistoryThreadGet thread = new CheckinHistoryThreadGet(listener, date);
        thread.start();
    }


    //default method for posting to an endpoint with JSON data, which handles errors, successes, timeouts, and their associated
    //listener methods
    public void post(BackEndListener listener, String endpoint,JSONObject json){
        try {
            //include secret app code in all communications (class "Code" is not on github)
            json.put("code",Code.CODE);

            //initialize connection properties
            URL url = new URL(baseUrl + endpoint);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            Memory memory = new Memory();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");

            //write JSON to HTTPS connection
            OutputStream os=urlConnection.getOutputStream();
            os.write(json.toString().getBytes());

            //wait for server response, or 5 second timeout
            code=-1;
            long start=System.currentTimeMillis();
            while(code==-1){
                code=urlConnection.getResponseCode();
                if(System.currentTimeMillis()-start>5000){//server timed out, call appropriate listener method
                    listener.onCancelled();
                    return;
                }
            }

            //parse server response
            String body="";
            if (code == 200) {//action was successful
                //parse message
                InputStream is = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = br.readLine()) != null) {
                    body += line;
                }
                //if user logged in, login user in local memory
                if(endpoint.equals("/login")) {
                    try {
                        JSONObject j = new JSONObject(body);
                        memory.loginUser(json.getString("email"),j.getString("token"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //call listener success method
                listener.onSuccess(body);
            } else {//action failed
                //parse error message
                InputStream is = new BufferedInputStream(urlConnection.getErrorStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = br.readLine()) != null) {
                    body += "" + line;
                }
                //call listener failure and pass error message
                listener.onFailure(body);
            }
        }catch(ProtocolException e){
            e.printStackTrace();;
        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }

    //thread for getting signin history
    public class CheckinThreadGet extends Thread{
        private BackEndListener listener;
        public CheckinThreadGet(BackEndListener listener){
            this.listener=listener;
        }
        public void run(){
            try {
                Memory memory = new Memory();
                JSONObject json = new JSONObject();
                json.put("token", memory.getToken());
                String date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                Log.d("CurrentDate", date);
                json.put("date", date);
                post(listener, "/getcheckins", json);
            } catch (org.json.JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class CheckinHistoryThreadGet extends Thread {
        private BackEndListener listener;
        private String date;

        public CheckinHistoryThreadGet(BackEndListener listener, String date) {
            this.listener = listener;
            this.date = date;
        }

        public void run() {
            try {
                Memory memory = new Memory();
                JSONObject json = new JSONObject();
                json.put("token", memory.getToken());
                Log.d("CurrentDate", date);
                json.put("date",date);
                post(listener, "/getcheckins", json);
            } catch(org.json.JSONException e){
                e.printStackTrace();
            }
        }
    }

    //thread for retrieving schedule
    public class ScheduleThreadGet extends Thread{
        private BackEndListener listener;
        public ScheduleThreadGet(BackEndListener listener){
            this.listener=listener;
        }
        public void run(){
            try {
                Memory memory = new Memory();
                JSONObject json = new JSONObject();
                json.put("token", memory.getToken());
                post(listener, "/getschedule", json);
            } catch(org.json.JSONException e){
                e.printStackTrace();
            }
        }
    }

    //thread for posting schedule
    public class ScheduleThreadPost extends Thread{
        private BackEndListener listener;
        public ScheduleThreadPost( BackEndListener listener){
            this.listener=listener;
        }
        public void run(){
            try {
                Memory memory=new Memory();
                JSONObject json=new JSONObject();
                json.put("monday",monday.isChecked());
                json.put("tuesday",tuesday.isChecked());
                json.put("wednesday",wednesday.isChecked());
                json.put("thursday",thursday.isChecked());
                json.put("friday",friday.isChecked());
                json.put("token",memory.getToken());
                post(listener,"/schedule",json);
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
    }

    //thread for signing in
    public class SigninThread extends Thread{
        private BackEndListener listener;
        public SigninThread(BackEndListener listener){
            this.listener=listener;
        }
        public void run(){
            try {
                JSONObject json=new JSONObject();
                Memory memory=new Memory();
                json.put("lat", latitude);
                json.put("lon", longitude);
                json.put("token",memory.getToken());
                post(listener,"/checkin",json);
            }catch(JSONException e){
             e.printStackTrace();
            }
        }
    }

    //thread for logging in
    public class LoginThread extends Thread{
        private BackEndListener listener;
        public LoginThread( BackEndListener listener){
            this.listener=listener;
        }
        public void run(){
            try {
                JSONObject user=new JSONObject();
                user.put("email", email.getText().toString());
                user.put("password", password.getText().toString());
                post(listener,"/login",user);
            }catch(org.json.JSONException e){
                e.printStackTrace();
            }
        }
    }

    //thread for registering
    public class RegisterThread extends Thread {
        private BackEndListener listener;
        private RegisterThread( BackEndListener listener){
            this.listener=listener;
        }
        public void run() {
            try {
                JSONObject user = new JSONObject();
                user.put("email", email.getText().toString());
                user.put("firstname", firstName.getText().toString());
                user.put("lastname", lastName.getText().toString());
                user.put("grade",grade.getText().toString());
                user.put("password", password.getText().toString());
                post(listener,"/register",user);
            }catch (org.json.JSONException e) {

            }
        }
    }
}

