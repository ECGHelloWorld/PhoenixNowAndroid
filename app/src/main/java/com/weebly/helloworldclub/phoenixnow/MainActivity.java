package com.weebly.helloworldclub.phoenixnow;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.titlepage);
    }
    public void loginActivity(View view){
        Intent intent=new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
    }
    public void registerActivity(View view){
        Intent intent =new Intent(MainActivity.this,RegisterActivity.class);
        startActivity(intent);
    }

}
