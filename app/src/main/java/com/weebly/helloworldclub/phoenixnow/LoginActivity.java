package com.weebly.helloworldclub.phoenixnow;


import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Justin on 3/1/2016.
 */
public class LoginActivity extends AppCompatActivity {
    /*
    This class handles the login form
     */
    //textview for displaying messages
    TextView tx;
    Typeface custom_font;

    //activity created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        custom_font = Typeface.createFromAsset(getAssets(), "fonts/CinzelDecorative.ttf");
        tx = (TextView) findViewById(R.id.title);
        tx.setTypeface(custom_font);
    }

    //user clicked login, so send data to server
    public void loginClick(View view) {
        EditText emailEditText = (EditText) findViewById(R.id.emailedittext);
        EditText passwordEditText = (EditText) findViewById(R.id.passwordedittext);
        final TextView space = (TextView) findViewById(R.id.space);
        BackEnd backend = new BackEnd(emailEditText, passwordEditText, null, null, null);
        makeToast("Logging in...");
        backend.login(new BackEnd.BackEndListener() {//send data to server
            @Override
            public void onSuccess(String data) {//login successful
                makeToast("Logged in!");
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(String message) {//login failed
                try {
                    JSONObject json = new JSONObject(message);
                    setText(json.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    setText("Incorrect password");
                }
            }

            @Override
            public void onCancelled() {//server timed out
                makeToast("Server request timed out");
            }
        });
    }

    //default method for making toasts
    public void makeToast(final String message) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //default method for settings textview
    public void setText(String message) {
        final TextView space = (TextView) findViewById(R.id.space);
        final String m = message;
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                space.setText(m);
            }
        });
    }

    //user clicked "forgot password", redirect them to website for PW reset
    public void forgotPassword(View view) {
        Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://phoenixnow.org/requestreset"));
        startActivity(browserIntent);
        makeToast("Redirecting to the PhoenixNow website for password reset");
    }
}
