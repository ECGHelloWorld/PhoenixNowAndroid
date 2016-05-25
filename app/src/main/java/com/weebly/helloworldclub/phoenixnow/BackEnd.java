package com.weebly.helloworldclub.phoenixnow;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by hunai on 3/4/2016.
 */
public class BackEnd {
    EditText email;
   EditText name;
    EditText password;
    static String emailstring;
    String message;
    int code;
    static String token;
    double latitude;
    double longitude;
    public BackEnd(EditText emailEditText, EditText passwordEditText, EditText nameEditText) {
        email=emailEditText;
        name=nameEditText;
        password=passwordEditText;
    }
    public BackEnd(){

    }

    public void register(RegisterActivity registerActivity) {
        RegisterThread thread = new RegisterThread(registerActivity.getBaseContext());
        thread.start();
    }
    public void login(LoginActivity loginActivity){
        LoginThread thread=new LoginThread(loginActivity.getBaseContext());
        thread.start();
    }
    public void signIn(double lat,double lon, MainActivity mainActivity){
        latitude=lat;
        longitude=lon;
        SigninThread thread=new SigninThread(mainActivity);
        thread.start();
    }
    public class SigninThread extends Thread{
        private MainActivity mainActivity;
        public SigninThread(MainActivity m){
            this.mainActivity=m;
        }
        public void run(){
            try {
                URL url=new URL("http://helloworldapi.nickendo.com/signins");
                HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();
                JSONObject json=new JSONObject();
                json.put("lat", latitude);
                json.put("lon", longitude);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection.setRequestProperty("Accept", "application/json");
                Memory memory=new Memory(mainActivity.getApplicationContext());
                urlConnection.setRequestProperty("Authorization",memory.getToken());
                OutputStream os=urlConnection.getOutputStream();
                os.write(json.toString().getBytes());
                Log.d("json", json.toString());
                String response=null;
                while(response==null){
                    response=urlConnection.getResponseMessage();
                }
                Log.d("SigninResponse", response);
                if(response.equalsIgnoreCase("conflict")){
                    mainActivity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(mainActivity.getBaseContext(), "Not at Guilford", Toast.LENGTH_LONG).show();
                        }
                    });
                }else if(response.equalsIgnoreCase("created")){
                    mainActivity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(mainActivity.getBaseContext(), "Signed in", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }catch(java.io.IOException e){
                e.printStackTrace();
            }catch(JSONException e){
             e.printStackTrace();
            }
        }
    }
    public class LoginThread extends Thread{
        private Context context;
        public LoginThread(Context c){
            this.context=c;
        }
        public void run(){
            try {
                message=null;
                URL url = new URL("http://helloworldapi.nickendo.com/login");
                HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();
                JSONObject user=new JSONObject();
                user.put("email", email.getText().toString());
                user.put("password", password.getText().toString());
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection.setRequestProperty("Accept", "application/json");
                OutputStream os=urlConnection.getOutputStream();
                os.write(user.toString().getBytes());
                code=-1;
                while (code == -1) {
                    code=urlConnection.getResponseCode();
                    message=urlConnection.getResponseMessage();
                }
                token=urlConnection.getHeaderField("Authorization");
                emailstring=email.getText().toString();
                Log.d("message",message);
                if(message.equals("OK")){
                    Memory memory=new Memory(context);
                    memory.loginUser(emailstring,token);
                }
            }catch(java.net.MalformedURLException e){
                e.printStackTrace();
            }catch(org.json.JSONException e){
                e.printStackTrace();
            }catch(java.net.ProtocolException e){
                e.printStackTrace();
            }catch(java.io.IOException e){
                e.printStackTrace();
            }
        }
    }

    public class RegisterThread extends Thread {
        private Context context;
        private RegisterThread(Context c){
            this.context=c;
        }
        public void run() {
            try {
                message=null;
                URL url = new URL("http://helloworldapi.nickendo.com/register");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                JSONObject user = new JSONObject();
                user.put("email", email.getText().toString());
                user.put("name", name.getText().toString());
                user.put("password", password.getText().toString());
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestMethod("POST");
                OutputStream os = urlConnection.getOutputStream();
                os.write(user.toString().getBytes());
                os.close();
                code=-1;
                while(code==-1){
                    code = urlConnection.getResponseCode();
                    message = urlConnection.getResponseMessage();
                }
                token=urlConnection.getHeaderField("Authorization");
                emailstring=email.getText().toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            } catch (org.json.JSONException e) {

            }
        }
    }
        public String getReturned() {
            return message;
        }

        public int getCode() {
            return code;
        }


}

