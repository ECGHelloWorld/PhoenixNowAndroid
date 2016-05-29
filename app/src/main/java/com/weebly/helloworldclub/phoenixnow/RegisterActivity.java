package com.weebly.helloworldclub.phoenixnow;

import android.content.Intent;
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
import java.util.Timer;
import java.util.TimerTask;

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

public void registerClick(View view){
    EditText passwordEditText=(EditText)findViewById(R.id.passwordedittext);
    EditText confirmPasswordEditText=(EditText)findViewById(R.id.confirmpasswordedittext);
    EditText emailEditText=(EditText)findViewById(R.id.usernameedittext);
    EditText nameEditText=(EditText)findViewById(R.id.nameedittext);
    final TextView space=(TextView)findViewById(R.id.space);
    space.setText("Pick a password with at least 8 characters");
    BackEnd backend=new BackEnd(emailEditText,passwordEditText,nameEditText);
    if(confirmPasswordEditText.getText().toString().equals(passwordEditText.getText().toString())&&
            confirmPasswordEditText.getText().toString().length()>7 &&
            emailEditText.getText().toString().contains("@guilford.edu") &&
            !nameEditText.getText().toString().equals("")){
        backend.register(this);
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while(backend.getReturned()==null){
        }
        if(backend.getReturned().equals("OK")&&backend.getCode()==200){
            space.setText("Registered!");
            Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
            startActivity(intent);
        }else if(backend.getReturned().equalsIgnoreCase("conflict")&&backend.getCode()==409){
            space.setText("User already registered");
        }
    }
    if(!confirmPasswordEditText.getText().toString().equals(passwordEditText.getText().toString())){
        Toast.makeText(getApplicationContext(), "Passwords must match", Toast.LENGTH_LONG).show();
    }else if(confirmPasswordEditText.getText().toString().length()<8){
        Toast.makeText(getApplicationContext(), "Password must be at least 8 characters", Toast.LENGTH_LONG).show();
    }else if(nameEditText.getText().toString().equals("")){
        Toast.makeText(getApplicationContext(), "Please input a name", Toast.LENGTH_LONG).show();
    }else if(!emailEditText.getText().toString().contains("@guilford.edu")){
        Toast.makeText(getApplicationContext(), "Please input a guilford email", Toast.LENGTH_LONG).show();
    }
}
}