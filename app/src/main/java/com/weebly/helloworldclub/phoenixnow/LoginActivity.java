package com.weebly.helloworldclub.phoenixnow;


import android.content.Intent;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }

    public void loginClick(View view) {
        EditText emailEditText = (EditText) findViewById(R.id.emailedittext);
        EditText passwordEditText = (EditText) findViewById(R.id.passwordedittext);
        final TextView space = (TextView) findViewById(R.id.space);
        BackEnd backend = new BackEnd(emailEditText, passwordEditText, null, null, null);
        makeToast("Logging in...");
        backend.login(new BackEnd.BackEndListener() {
            @Override
            public void onSuccess(String data) {
                makeToast("Logged in!");
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(String message) {
                try {
                    JSONObject json = new JSONObject(message);
                    setText(json.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    setText("Incorrect password");
                }
            }

            @Override
            public void onCancelled() {
                makeToast("Server request timed out");
            }
        });
    }

    public void makeToast(final String message) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

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

    public void forgotPassword(View view) {

        Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://phoenixnow.org/requestreset"));
        startActivity(browserIntent);
        Toast.makeText(getApplicationContext(), "Redirecting to the PhoenixNow website for password reset.", Toast.LENGTH_LONG).show();

    }
}
