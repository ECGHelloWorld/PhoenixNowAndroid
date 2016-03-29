package com.weebly.helloworldclub.phoenixnow;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


/**
 * Created by Justin on 3/1/2016.
 */
public class LoginActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }
    public void loginClick(View view){
        EditText emailEditText=(EditText)findViewById(R.id.emailedittext);
        EditText passwordEditText=(EditText)findViewById(R.id.passwordedittext);
        TextView space=(TextView)findViewById(R.id.space);
        BackEnd backend=new BackEnd(emailEditText,passwordEditText,null);
        backend.login();
        long start=System.currentTimeMillis();
        while(System.currentTimeMillis()<start+1500){
        }
        if(backend.getReturned().equals("OK")&&backend.getCode()==200){
            space.setText("Logged in!");
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        }else if(backend.getReturned().equalsIgnoreCase("unauthorized")&&backend.getCode()==401){
            space.setText("User not in system");
        }
    }

}
