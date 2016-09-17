package com.weebly.helloworldclub.phoenixnow;

import android.content.Intent;
import android.graphics.Typeface;
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
    /*
    This class handles the registration form
     */
    private Toast toast;
    TextView tx;
    Typeface custom_font;

    //activity started
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        custom_font = Typeface.createFromAsset(getAssets(), "fonts/CinzelDecorative.ttf");
        tx = (TextView) findViewById(R.id.title);
        tx.setTypeface(custom_font);
        TextView space=(TextView)findViewById(R.id.space);
        space.setText("Pick a password with at least 8 characters");
    }

    //user clicked register, send information to server if valid
    public void registerClick(View view){
        EditText passwordEditText=(EditText)findViewById(R.id.passwordedittext);
        EditText confirmPasswordEditText=(EditText)findViewById(R.id.confirmpasswordedittext);
        EditText emailEditText=(EditText)findViewById(R.id.usernameedittext);
        EditText firstNameEditText=(EditText)findViewById(R.id.firstNameEditText);
        EditText lastNameEditText=(EditText)findViewById(R.id.lastNameEditText);
        EditText gradeEditText=(EditText)findViewById(R.id.gradeEditText);
        BackEnd backend=new BackEnd(emailEditText,passwordEditText,firstNameEditText,lastNameEditText,gradeEditText);
        if(confirmPasswordEditText.getText().toString().equals(passwordEditText.getText().toString())&&//PW and confirm PW are identical
                confirmPasswordEditText.getText().toString().length()>7 &&//PW is at least 8 characters
                emailEditText.getText().toString().contains("@guilford.edu") &&//email includes "guilford.edu"
                !firstNameEditText.getText().toString().equals("")&&//name is not blank
                !lastNameEditText.getText().toString().equals("")&&//name is not blank
                gradeEditText.getText().length()>0){//grade is not blank
            makeToast("Registering...");
            backend.register(new BackEnd.BackEndListener() {//register user
                @Override
                public void onSuccess(String data) {//user successfully registered
                    makeToast("Registered!");
                    Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(String message) {//registration failed
                    try {
                        JSONObject json=new JSONObject(message);
                        setText(json.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled() {//server timed out
                    makeToast("Server request timed out");
                }
            });
        }

        //toasts handling invalid form information
        if(!confirmPasswordEditText.getText().toString().equals(passwordEditText.getText().toString())){
            makeToast("Passwords must match");
        }else if(confirmPasswordEditText.getText().toString().length()<8){
           makeToast("Password must be at least 8 characters");
        }else if(firstNameEditText.getText().toString().equals("")){
            makeToast("Please input a first name");
        }else if(!emailEditText.getText().toString().contains("@guilford.edu")){
            makeToast("Please input a guilford email");
        }else if(lastNameEditText.toString().equals("")){
            makeToast("Please input a last name");
        }else if(gradeEditText.getText().length()==0){
            makeToast("Please input a grade level");
        }

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

    //method for setting text
    public void setText(String message){
        final TextView space=(TextView)findViewById(R.id.space);
        final String m=message;
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                space.setText(m);
            }
        });
    }
}