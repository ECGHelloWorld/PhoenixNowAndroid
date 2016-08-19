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

/**
 * Created by hunai on 3/4/2016.
 */
public class BackEnd {
    public interface BackEndListener{
        public void onSuccess(String data);
        public void onFailure(String message);
        public void onCancelled();
    }
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
    private static final String baseUrl="http://chadali.me/api";

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

    public void register(RegisterActivity registerActivity,BackEndListener listener) {
        RegisterThread thread = new RegisterThread(registerActivity.getBaseContext(),listener);
        thread.start();
    }
    public void login(LoginActivity loginActivity,BackEndListener listener){
        LoginThread thread=new LoginThread(loginActivity.getBaseContext(),listener);
        thread.start();
    }
    public void signIn(double lat,double lon, MainActivity mainActivity,BackEndListener listener){
        latitude=lat;
        longitude=lon;
        SigninThread thread=new SigninThread(mainActivity,listener);
        thread.start();
    }
    public void sendSchedule(ScheduleActivity scheduleActivity,BackEndListener listener){
        ScheduleThreadPost thread=new ScheduleThreadPost(scheduleActivity,listener);
        thread.start();
    }
    public void getSchedule(ScheduleActivity scheduleActivity,BackEndListener listener){
        ScheduleThreadGet thread=new ScheduleThreadGet(scheduleActivity,listener);
        thread.start();
    }
    public void post(BackEndListener listener, String endpoint,JSONObject json){
        try {
            URL url = new URL(baseUrl + endpoint);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            Memory memory = new Memory();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            OutputStream os=urlConnection.getOutputStream();
            os.write(json.toString().getBytes());
            code=-1;
            long start=System.currentTimeMillis();
            while(code==-1){
                code=urlConnection.getResponseCode();
                if(System.currentTimeMillis()-start>5000){
                    listener.onCancelled();
                    return;
                }
            }
            String body="";
            if (code == 200) {
                InputStream is = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = br.readLine()) != null) {
                    body += line;
                }
                if(endpoint.equals("/login")) {
                    try {
                        JSONObject j = new JSONObject(body);
                        memory.loginUser(json.getString("email"),j.getString("token"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                listener.onSuccess(body);
            } else {
                InputStream is = new BufferedInputStream(urlConnection.getErrorStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = br.readLine()) != null) {
                    body += "" + line;
                }
                listener.onFailure(body);
            }
        }catch(ProtocolException e){
            e.printStackTrace();;
        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public class ScheduleThreadGet extends Thread{
        private ScheduleActivity scheduleActivity;
        private BackEndListener listener;
        public ScheduleThreadGet(ScheduleActivity a, BackEndListener listener){
            this.scheduleActivity=a;
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
    public class ScheduleThreadPost extends Thread{
        private ScheduleActivity scheduleActivity;
        private BackEndListener listener;
        public ScheduleThreadPost(ScheduleActivity s, BackEndListener listener){
            this.scheduleActivity=s;
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
    public class SigninThread extends Thread{
        private MainActivity mainActivity;
        private BackEndListener listener;
        public SigninThread(MainActivity m, BackEndListener listener){
            this.mainActivity=m;
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
    public class LoginThread extends Thread{
        private BackEndListener listener;
        public LoginThread(Context c, BackEndListener listener){
            this.listener=listener;
        }
        public void run(){
            try {
                JSONObject user=new JSONObject();
                user.put("email", email.getText().toString());
                user.put("password", password.getText().toString());
                user.put("code",Integer.toString(Code.CODE));
                post(listener,"/login",user);
            }catch(org.json.JSONException e){
                e.printStackTrace();
            }
        }
    }

    public class RegisterThread extends Thread {
        private BackEndListener listener;
        private RegisterThread(Context c, BackEndListener listener){
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
//                user.put("code",Integer.toString(Code.CODE));
                post(listener,"/register",user);
            }catch (org.json.JSONException e) {

            }
        }
    }
}

