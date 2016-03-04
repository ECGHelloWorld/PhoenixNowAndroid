package com.weebly.helloworldclub.phoenixnow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.*;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

/**
 * Created by Justin on 3/1/2016.
 */
public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
    }
    public class MyThread extends Thread{
        String message;
        int code;
        public void run(){
            try {
                EditText emailEditText=(EditText)findViewById(R.id.usernameedittext);
                EditText passwordEditText=(EditText)findViewById(R.id.passwordedittext);
                EditText nameEditText=(EditText)findViewById(R.id.nameedittext);
                URL url = new URL("http://helloworldapi.nickendo.com/register");
                HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();
                JSONObject user=new JSONObject();
                user.put("email", emailEditText.getText().toString());
                user.put("name", nameEditText.getText().toString());
                user.put("password", passwordEditText.getText().toString());
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestMethod("POST");
                OutputStream os=urlConnection.getOutputStream();
                os.write(user.toString().getBytes());
                os.close();
                code=urlConnection.getResponseCode();
                message=urlConnection.getResponseMessage();
                Log.d("responsetime","reponse");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }catch(java.io.IOException e){
                e.printStackTrace();
            }catch(org.json.JSONException e){

            }
        }
        public String getReturned(){
            return message;
        }
        public int getCode(){return code;}
    }
public void registerClick(View view){
    EditText passwordEditText=(EditText)findViewById(R.id.passwordedittext);
    EditText confirmPasswordEditText=(EditText)findViewById(R.id.confirmpasswordedittext);
    TextView space=(TextView)findViewById(R.id.space);
    MyThread thread=new MyThread();
    if(confirmPasswordEditText.getText().toString().equals(passwordEditText.getText().toString())){
        thread.start();
        long start=System.currentTimeMillis();
        while(System.currentTimeMillis()<start+1000){

        }
        if(thread.getReturned().equals("OK")&&thread.getCode()==200){
            space.setText("Registered!");
        }else if(thread.getReturned().equalsIgnoreCase("conflict")&&thread.getCode()==409){
            space.setText("User already registered");
        }
        Log.d("retrieved time", "retrieved time");
    }else{
        space.setText("Passwords must match");
    }
}


}
