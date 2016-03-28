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
    TextView space=(TextView)findViewById(R.id.space);
    BackEnd backend=new BackEnd(emailEditText,passwordEditText,nameEditText);
    if(confirmPasswordEditText.getText().toString().equals(passwordEditText.getText().toString())){
        backend.register();
        long start=System.currentTimeMillis();
        while(System.currentTimeMillis()<start+1000){

        }
        if(backend.getReturned().equals("OK")&&backend.getCode()==200){
            space.setText("Registered!");
            Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
            startActivity(intent);
        }else if(backend.getReturned().equalsIgnoreCase("conflict")&&backend.getCode()==409){
            space.setText("User already registered");
        }
    }else{
        space.setText("Passwords must match");
    }
}
}