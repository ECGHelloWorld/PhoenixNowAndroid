package com.weebly.helloworldclub.phoenixnow;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


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
        final TextView space=(TextView)findViewById(R.id.space);
        BackEnd backend=new BackEnd(emailEditText,passwordEditText,null);
        backend.login(this);
         try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while(backend.getReturned()==null){
        }
        if(backend.getCode()==200){
            space.setText("Logged in!");
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        }else if(backend.getCode()==401){
            space.setText("User not in system");
        }else if(backend.getCode()==201){
           space.setText("Please include PIN");
        }else if(backend.getCode()==202){
           space.setText("Incorrect PIN");
        }
    }
}
